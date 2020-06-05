package com.github.bosik927.database.control;

import com.github.bosik927.database.entity.ReservationDTO;
import com.github.bosik927.database.entity.ReservationEntity;
import com.github.bosik927.database.entity.RoomEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class ReceptionController {

    private final ReceptionService receptionService;

    @Autowired
    public ReceptionController(ReceptionService receptionService) {
        this.receptionService = receptionService;
    }

    @RequestMapping("/rooms")
    public List<RoomEntity> getAvailableRooms() {
        return receptionService.getAvailableRooms();
    }

    @PostMapping("/reservation")
    public ReservationEntity createReservation(@RequestBody ReservationDTO reservationDTO) {
        return receptionService.createReservation(reservationDTO);
    }

    @DeleteMapping("/reservation/{id}")
    public void deleteReservation(@PathVariable Long id) {
        receptionService.deleteReservation(id);
    }

}
