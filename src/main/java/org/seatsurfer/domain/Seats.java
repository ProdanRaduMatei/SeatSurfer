package org.seatsurfer.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "Seats")
@Table(
        name = "seats"
)
public class Seats {
    @Id
    @SequenceGenerator(
            name = "seat_sequence",
            sequenceName = "seat_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "seat_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "row",
            nullable = false
    )
    private Integer row;

    @Column(
            name = "col",
            nullable = false
    )
    private Integer col;

    @Column(
            name = "is_seat",
            nullable = false
    )
    private Boolean isSeat;

    @Column(
            name = "is_occupied",
            nullable = false
    )
    private Boolean isOccupied;

    @Column(
            name = "occupied_date",
            nullable = false
    )
    private Date occupiedDate;

    @Column(
            name = "storey_id",
            nullable = false
    )
    private Long storeyId;

    @Column(
            name = "user_id"
    )
    private Long userId;

    @Column(
            name = "admin_id",
            nullable = false
    )
    private Long adminId;

    public Seats() {
    }

    public Seats(Integer row, Integer col, Boolean isSeat, Boolean isOccupied, Date occupiedDate, Long storeyId, Long userId, Long adminId) {
        this.row = row;
        this.col = col;
        this.isSeat = isSeat;
        this.isOccupied = isOccupied;
        this.occupiedDate = occupiedDate;
        this.storeyId = storeyId;
        this.userId = userId;
        this.adminId = adminId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Boolean getSeat() {
        return isSeat;
    }

    public void setSeat(Boolean seat) {
        isSeat = seat;
    }

    public Boolean getOccupied() {
        return isOccupied;
    }

    public void setOccupied(Boolean occupied) {
        isOccupied = occupied;
    }

    public Date getOccupiedDate() {
        return occupiedDate;
    }

    public void setOccupiedDate(Date occupiedDate) {
        this.occupiedDate = occupiedDate;
    }

    public Long getStoreyId() {
        return storeyId;
    }

    public void setStoreyId(Long storeyId) {
        this.storeyId = storeyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "Seats{" +
                "id=" + id +
                ", row=" + row +
                ", col=" + col +
                ", isSeat=" + isSeat +
                ", isOccupied=" + isOccupied +
                ", occupiedDate=" + occupiedDate +
                ", storeyId=" + storeyId +
                ", userId=" + userId +
                ", adminId=" + adminId +
                '}';
    }
}
