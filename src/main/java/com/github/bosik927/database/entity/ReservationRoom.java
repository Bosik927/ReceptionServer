package com.github.bosik927.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reservations_rooms")
public class ReservationRoom {

    @Id
    @GeneratedValue
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
}
