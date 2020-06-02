package com.github.bosik927.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "reservation_date")
    private java.sql.Date reservationDate;


    public Long getReservationId() {
        return this.reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public java.sql.Date getReservationDate() {
        return this.reservationDate;
    }

    public void setReservationDate(java.sql.Date reservationDate) {
        this.reservationDate = reservationDate;
    }
}
