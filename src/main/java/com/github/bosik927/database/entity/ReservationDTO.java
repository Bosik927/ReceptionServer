package com.github.bosik927.database.entity;

import java.sql.Date;
import java.util.List;

public class ReservationDTO {

    private Long customer_id;
    private Date reservationDate;
    private List<Long> roomsIds;

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public List<Long> getRoomsIds() {
        return roomsIds;
    }

    public void setRoomsIds(List<Long> roomsIds) {
        this.roomsIds = roomsIds;
    }
}
