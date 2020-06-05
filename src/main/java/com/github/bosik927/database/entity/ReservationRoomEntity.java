package com.github.bosik927.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "reservations_rooms")
public class ReservationRoomEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservations_rooms_id")
    private Long reservationsRoomsId;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "room_id")
    private Long roomId;


    public Long getReservationsRoomsId() {
        return this.reservationsRoomsId;
    }

    public void setReservationsRoomsId(Long reservationsRoomsId) {
        this.reservationsRoomsId = reservationsRoomsId;
    }

    public Long getReservationId() {
        return this.reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getRoomId() {
        return this.roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationRoomEntity)) return false;
        ReservationRoomEntity that = (ReservationRoomEntity) o;
        return Objects.equals(getReservationsRoomsId(), that.getReservationsRoomsId()) &&
                Objects.equals(getReservationId(), that.getReservationId()) &&
                Objects.equals(getRoomId(), that.getRoomId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReservationsRoomsId(), getReservationId(), getRoomId());
    }
}
