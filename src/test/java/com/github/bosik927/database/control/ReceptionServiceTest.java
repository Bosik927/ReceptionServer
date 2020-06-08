package com.github.bosik927.database.control;

import com.github.bosik927.database.boundary.ReservationRepository;
import com.github.bosik927.database.boundary.ReservationRoomRepository;
import com.github.bosik927.database.boundary.RoomRepository;
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

@RunWith(MockitoJUnitRunner.class)
public class ReceptionServiceTest {

    @Mock
    RoomRepository roomRepository;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ReservationRoomRepository reservationRoomRepository;
    @InjectMocks
    ReceptionService receptionService;

    @Test
    public void testAvailableRooms_allOpenRooms() {
        List<RoomEntity> allRooms = new ArrayList<>();
        RoomEntity openRoom = RoomEntity.builder()
                .isLocked(false).build();
        allRooms.add(openRoom);

        RoomEntity openRoom2 = RoomEntity.builder()
                .isLocked(false).build();
        allRooms.add(openRoom2);

        when(roomRepository.findAll()).thenReturn(allRooms);

        assertEquals(allRooms, receptionService.getAvailableRooms());
    }

    @Test
    public void testAvailableRooms_oneOpenOneClosedRoom() {
        List<RoomEntity> allRooms = new ArrayList<>();
        List<RoomEntity> openRooms = new ArrayList<>();


        RoomEntity openRoom = RoomEntity.builder()
                .isLocked(false).build();
        allRooms.add(openRoom);
        openRooms.add(openRoom);

        RoomEntity closeRoom = RoomEntity.builder()
                .isLocked(true).build();
        allRooms.add(closeRoom);

        when(roomRepository.findAll()).thenReturn(allRooms);

        assertEquals(openRooms, receptionService.getAvailableRooms());
    }

    @Test
    public void testAvailableRooms_allClosedRooms() {
        List<RoomEntity> allRooms = new ArrayList<>();

        RoomEntity closeRoom1 = RoomEntity.builder()
                .isLocked(true).build();
        allRooms.add(closeRoom1);

        RoomEntity closeRoom2 = RoomEntity.builder()
                .isLocked(true).build();
        allRooms.add(closeRoom2);

        when(roomRepository.findAll()).thenReturn(allRooms);

        assertEquals(new ArrayList<>(), receptionService.getAvailableRooms());
    }

    @Test
    public void testCreateReservation_testReturnValueAndNumberRepositoriesInvocations() {
        Date simulatedDate = Date.valueOf(LocalDate.now());
        Long simulatedCustomerId = 1L;
        Long simulatedReservationId = 3L;
        List<Long> simulatedRoomsIds = Arrays.asList(1L, 2L);

        ReservationDTO simulatedDto = createSimulateDto(simulatedCustomerId, simulatedDate, simulatedRoomsIds);

        ReservationEntity expectedToSaveReservation = new ReservationEntity();
        expectedToSaveReservation.setReservationDate(simulatedDate);
        expectedToSaveReservation.setCustomerId(simulatedCustomerId);

        ReservationEntity simulatedSavedReservation = new ReservationEntity();
        simulatedSavedReservation.setCustomerId(simulatedCustomerId);
        simulatedSavedReservation.setReservationDate(simulatedDate);
        simulatedSavedReservation.setReservationId(simulatedReservationId);

        ReservationRoomEntity expectedReservationRoom1 =
                createExpectedReservationRoom(simulatedReservationId, simulatedRoomsIds.get(0));
        ReservationRoomEntity expectedReservationRoom2 =
                createExpectedReservationRoom(simulatedReservationId, simulatedRoomsIds.get(1));

        RoomEntity simulatedRoom1 = RoomEntity.builder().withRoomId(simulatedRoomsIds.get(0))
                .withRoomNumber("2")
                .withMaxPersons(5)
                .isLocked(false).build();

        RoomEntity expectedToSaveRoom1 = RoomEntity.builder().withRoomId(simulatedRoomsIds.get(0))
                .withRoomNumber("2")
                .withMaxPersons(5)
                .isLocked(true).build();

        RoomEntity simulatedRoom2 = RoomEntity.builder().withRoomId(simulatedRoomsIds.get(1))
                .withRoomNumber("3")
                .withMaxPersons(4)
                .isLocked(false).build();

        RoomEntity expectedToSaveRoom2 = RoomEntity.builder().withRoomId(simulatedRoomsIds.get(1))
                .withRoomNumber("3")
                .withMaxPersons(4)
                .isLocked(true).build();

        when(reservationRepository.save(expectedToSaveReservation))
                .thenReturn(simulatedSavedReservation);
        when(reservationRoomRepository.save(any())).thenReturn(new ReservationRoomEntity());
        when(roomRepository.findById(simulatedRoomsIds.get(0)))
                .thenReturn(java.util.Optional.of(simulatedRoom1));
        when(roomRepository.findById(simulatedRoomsIds.get(1)))
                .thenReturn(java.util.Optional.of(simulatedRoom2));

        assertEquals(receptionService.createReservation(simulatedDto), simulatedSavedReservation);

        verify(reservationRepository).save(expectedToSaveReservation);

        verify(reservationRoomRepository, times(2)).save(any());
        verify(reservationRoomRepository).save(expectedReservationRoom1);
        verify(reservationRoomRepository).save(expectedReservationRoom2);

        verify(roomRepository, times(2)).save(any());
        verify(roomRepository).save(expectedToSaveRoom1);
        verify(roomRepository).save(expectedToSaveRoom2);
}


    @Test
    public void testDeleteReservation_testRepositories() {
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

        RoomEntity simulatedRoom1 = RoomEntity.builder()
                .withMaxPersons(5)
                .withRoomNumber("1")
                .withRoomId(1L)
                .isLocked(true).build();
        RoomEntity simulatedRoom2 = RoomEntity.builder()
                .withMaxPersons(10)
                .withRoomNumber("2")
                .withRoomId(2L)
                .isLocked(true).build();


        when(reservationRoomRepository.findAll()).thenReturn(reservationRooms);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(simulatedRoom1));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(simulatedRoom2));

        receptionService.deleteReservation(simulatedReservationId);

        verify(reservationRoomRepository).deleteById(100L);
        verify(reservationRoomRepository).deleteById(200L);
        verify(reservationRepository).deleteById(4L);
        verify(roomRepository).save(
                RoomEntity.builder().withMaxPersons(5)
                        .withRoomId(1L)
                        .withRoomNumber("1")
                        .isLocked(false).build());
        verify(roomRepository).save(
                RoomEntity.builder().withMaxPersons(10)
                        .withRoomId(2L)
                        .withRoomNumber("2")
                        .isLocked(false).build());
    }

    private ReservationDTO createSimulateDto(Long customerId, Date date, List<Long> roomsIds) {
        ReservationDTO simulateDto = new ReservationDTO();
        simulateDto.setCustomer_id(customerId);
        simulateDto.setReservationDate(date);
        simulateDto.setRoomsIds(roomsIds);
        return simulateDto;
    }

    private ReservationRoomEntity createExpectedReservationRoom(Long reservationId, Long roomId) {
        ReservationRoomEntity reservationRoom = new ReservationRoomEntity();
        reservationRoom.setReservationId(reservationId);
        reservationRoom.setRoomId(roomId);
        return reservationRoom;
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
