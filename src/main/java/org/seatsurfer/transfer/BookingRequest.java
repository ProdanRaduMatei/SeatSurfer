package org.seatsurfer.transfer;

import java.time.Instant;

public class BookingRequest {
    private Long userId;
    private Long seatId;
    private String date;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSeatId() {
        return seatId;
    }
    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}