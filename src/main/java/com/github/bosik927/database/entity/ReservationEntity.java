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
//TODO: Delete Builder
public class ReservationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "reservation_date")
    private Date reservationDate;

    protected ReservationEntity() {
    }

    private ReservationEntity(Long reservationId, Long customerId, Date reservationDate) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.reservationDate = reservationDate;
    }

    public Long getReservationId() {
        return this.reservationId;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public Date getReservationDate() {
        return this.reservationDate;
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

    public static Builder builder(){
        return new Builder();
    }

    static public class Builder {
        private Long reservationId;
        private Long customerId;
        private Date reservationDate;

        public Builder withReservationId(Long reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public Builder withCustomerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withReservationDate(Date reservationDate) {
            this.reservationDate = reservationDate;
            return this;
        }

        public ReservationEntity build() {
            return new ReservationEntity(reservationId, customerId, reservationDate);
        }
    }
}
