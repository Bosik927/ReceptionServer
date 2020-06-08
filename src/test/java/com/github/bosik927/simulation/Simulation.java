package com.github.bosik927.simulation;

import com.github.bosik927.database.boundary.ReservationRepository;
import com.github.bosik927.database.boundary.ReservationRoomRepository;
import com.github.bosik927.database.boundary.RoomRepository;
import com.github.bosik927.database.control.ReceptionController;
import com.github.bosik927.database.control.ReceptionService;
import com.github.bosik927.database.entity.ReservationDTO;
import com.github.bosik927.database.entity.ReservationEntity;
import com.github.bosik927.database.entity.ReservationRoomEntity;
import com.github.bosik927.database.entity.RoomEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Simulation {

    @InjectMocks
    ReceptionService receptionService;
    @Mock
    RoomRepository roomRepository;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ReservationRoomRepository reservationRoomRepository;

    @Test
    public void simulation() {
        ReservationDTO reservationDTO = createReservationDto();
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setCustomerId(reservationDTO.getCustomer_id());
        reservationEntity.setReservationDate(reservationDTO.getReservationDate());

        ReservationEntity simulatedReservation = new ReservationEntity();
        simulatedReservation.setCustomerId(reservationDTO.getCustomer_id());
        simulatedReservation.setReservationDate(reservationDTO.getReservationDate());
        simulatedReservation.setReservationId(1L);

        List<ReservationRoomEntity> reservationRooms = createReservationRooms();

        when(roomRepository.findAll()).thenReturn(simulateRooms());
        when(reservationRepository.save(reservationEntity)).thenReturn(simulatedReservation);
        when(roomRepository.findById(any())).thenReturn(
                Optional.ofNullable(RoomEntity.builder().build())
        );
        when(reservationRoomRepository.findAll()).thenReturn(reservationRooms);

        //1) Get rooms scenario
        assertEquals(openSimulateRooms(), receptionService.getAvailableRooms());

        //2) Create reservation
        receptionService.createReservation(reservationDTO);

        verify(reservationRepository, times(1)).save(any());
        verify(reservationRoomRepository, times(3)).save(any());
        verify(roomRepository, times(3)).save(any());

        //3) Delete reservation
        receptionService.deleteReservation(4L);

        verify(reservationRoomRepository, times(2)).deleteById(any());
        verify(reservationRepository).deleteById(4L);
        verify(reservationRoomRepository, times(3)).save(any());

    }

    private List<RoomEntity> simulateRooms() {
        List<RoomEntity> rooms = new ArrayList<>();
        rooms.add(RoomEntity.builder()
                .withRoomNumber("1")
                .withMaxPersons(5)
                .withRoomId(1L)
                .isLocked(false).build());

        rooms.add(RoomEntity.builder()
                .withRoomNumber("2a")
                .withMaxPersons(6)
                .withRoomId(2L)
                .isLocked(false).build());

        rooms.add(RoomEntity.builder()
                .withRoomNumber("2b")
                .withMaxPersons(7)
                .withRoomId(3L)
                .isLocked(false).build());

        rooms.add(RoomEntity.builder()
                .withRoomNumber("3")
                .withMaxPersons(8)
                .withRoomId(4L)
                .isLocked(true).build());

        rooms.add(RoomEntity.builder()
                .withRoomNumber("4")
                .withMaxPersons(9)
                .withRoomId(5L)
                .isLocked(true).build());

        return rooms;
    }

    private List<RoomEntity> openSimulateRooms() {
        List<RoomEntity> rooms = new ArrayList<>();
        rooms.add(RoomEntity.builder()
                .withRoomNumber("1")
                .withMaxPersons(5)
                .withRoomId(1L)
                .isLocked(false).build());

        rooms.add(RoomEntity.builder()
                .withRoomNumber("2a")
                .withMaxPersons(6)
                .withRoomId(2L)
                .isLocked(false).build());

        rooms.add(RoomEntity.builder()
                .withRoomNumber("2b")
                .withMaxPersons(7)
                .withRoomId(3L)
                .isLocked(false).build());
        return rooms;
    }

    private ReservationDTO createReservationDto() {
        Date simulatedDate = Date.valueOf(LocalDate.now().plusDays(10));
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setRoomsIds(Arrays.asList(1L, 2L, 3L));
        reservationDTO.setReservationDate(simulatedDate);
        reservationDTO.setCustomer_id(1L);
        return reservationDTO;
    }

    private List<ReservationRoomEntity> createReservationRooms(){
        Long simulatedReservationId = 4L;
        List<ReservationRoomEntity> reservationRooms = new ArrayList<>();
        ReservationRoomEntity reservationRoom1 =
                createSimulatedReservationRoom(simulatedReservationId, 1L, 100L);
        reservationRooms.add(reservationRoom1);
        ReservationRoomEntity reservationRoom2 =
                createSimulatedReservationRoom(simulatedReservationId, 2L, 200L);
        reservationRooms.add(reservationRoom2);
        ReservationRoomEntity reservationRoom3 =
                createSimulatedReservationRoom(1L, 3L, 300L);
        reservationRooms.add(reservationRoom3);
        ReservationRoomEntity reservationRoom4 =
                createSimulatedReservationRoom(1L, 4L, 400L);
        reservationRooms.add(reservationRoom4);
        return reservationRooms;
    }

    private ReservationRoomEntity createSimulatedReservationRoom(Long reservationId, Long roomId,
                                                                 Long reservationRoomId) {
        ReservationRoomEntity reservationRoom = new ReservationRoomEntity();
        reservationRoom.setReservationId(reservationId);
        reservationRoom.setRoomId(roomId);
        reservationRoom.setReservationsRoomsId(reservationRoomId);
        return reservationRoom;
    }
}
