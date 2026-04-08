package com.seatsurfer.service;

import com.seatsurfer.dto.BookingDtos;
import com.seatsurfer.entity.Booking;
import com.seatsurfer.entity.BookingAuditEvent;
import com.seatsurfer.entity.Seat;
import com.seatsurfer.model.BookingEventType;
import com.seatsurfer.repository.BookingAuditEventRepository;
import com.seatsurfer.repository.BookingRepository;
import com.seatsurfer.repository.SeatRepository;
import com.seatsurfer.util.NameUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final BookingAuditEventRepository bookingAuditEventRepository;

    public BookingService(
        BookingRepository bookingRepository,
        SeatRepository seatRepository,
        BookingAuditEventRepository bookingAuditEventRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.bookingAuditEventRepository = bookingAuditEventRepository;
    }

    @Transactional
    public BookingDtos.BookingResponse createBooking(BookingDtos.CreateBookingRequest request) {
        if (request.bookingDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bookings can only be made for today or future dates");
        }

        String normalizedName = NameUtils.normalizePersonName(request.bookedByName());
        if (bookingRepository.existsByBookedByNormalizedNameAndBookingDate(normalizedName, request.bookingDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This person already has a seat booked for that date");
        }

        Seat seat = seatRepository.findById(request.seatId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        if (!seat.getFloor().isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected floor is not active");
        }
        if (seat.getEffectiveFrom().isAfter(request.bookingDate())
            || (seat.getEffectiveTo() != null && seat.getEffectiveTo().isBefore(request.bookingDate()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not available on that date");
        }

        Booking booking = new Booking();
        booking.setSeat(seat);
        booking.setBookingDate(request.bookingDate());
        booking.setBookedByName(request.bookedByName().trim());
        booking.setBookedByNormalizedName(normalizedName);
        booking.setBookedByEmail(request.bookedByEmail());

        try {
            Booking saved = bookingRepository.saveAndFlush(booking);
            recordAudit(saved, saved.getBookedByName(), BookingEventType.BOOKED);
            return toResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That seat is already booked for the selected date");
        }
    }

    @Transactional
    public void cancelBooking(Long bookingId, String requesterName) {
        Booking booking = bookingRepository.findDetailedById(bookingId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Past bookings can no longer be canceled");
        }

        String requester = NameUtils.normalizePersonName(requesterName);
        if (!booking.getBookedByNormalizedName().equals(requester)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the person who created the booking can cancel it");
        }

        recordAudit(booking, requesterName.trim(), BookingEventType.CANCELED_BY_USER);
        bookingRepository.delete(booking);
    }

    @Transactional
    public void cancelBookingsForReconfiguration(Collection<Long> seatIds, LocalDate fromDate) {
        if (seatIds.isEmpty()) {
            return;
        }
        List<Booking> bookings = bookingRepository.findBySeatIdInAndBookingDateGreaterThanEqual(seatIds, fromDate);
        for (Booking booking : bookings) {
            recordAudit(booking, "Seat reconfiguration", BookingEventType.CANCELED_BY_RECONFIGURATION);
        }
        bookingRepository.deleteAll(bookings);
    }

    private BookingDtos.BookingResponse toResponse(Booking booking) {
        return new BookingDtos.BookingResponse(
            booking.getId(),
            booking.getSeat().getId(),
            booking.getSeat().getLabel(),
            booking.getSeat().getFloor().getId(),
            booking.getSeat().getFloor().getName(),
            booking.getBookingDate(),
            booking.getBookedByName(),
            booking.getBookedByEmail(),
            booking.getCreatedAt()
        );
    }

    private void recordAudit(Booking booking, String actorName, BookingEventType eventType) {
        BookingAuditEvent event = new BookingAuditEvent();
        event.setFloor(booking.getSeat().getFloor());
        event.setSeat(booking.getSeat());
        event.setBookingId(booking.getId());
        event.setSeatLabel(booking.getSeat().getLabel());
        event.setBookingDate(booking.getBookingDate());
        event.setActorName(actorName);
        event.setEventType(eventType);
        event.setCreatedAt(LocalDateTime.now());
        bookingAuditEventRepository.save(event);
    }
}
