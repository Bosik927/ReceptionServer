package com.github.bosik927.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "reservations")
public class ReservationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "reservation_date")
    private Date reservationDate;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationEntity)) return false;
        ReservationEntity that = (ReservationEntity) o;
        return Objects.equals(getReservationId(), that.getReservationId()) &&
                Objects.equals(getCustomerId(), that.getCustomerId()) &&
                Objects.equals(getReservationDate(), that.getReservationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReservationId(), getCustomerId(), getReservationDate());
    }
}
