package com.seatsurfer.repository;

import com.seatsurfer.entity.BookingAuditEvent;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingAuditEventRepository extends JpaRepository<BookingAuditEvent, Long> {

    List<BookingAuditEvent> findByBookingDateBetween(LocalDate from, LocalDate to);
}
