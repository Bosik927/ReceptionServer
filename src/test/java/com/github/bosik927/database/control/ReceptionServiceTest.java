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
import static org.mockito.Mockito.*;

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
        RoomEntity openRoom = new RoomEntity();
        openRoom.setIsLocked(false);
        allRooms.add(openRoom);

        RoomEntity openRoom2 = new RoomEntity();
        openRoom2.setIsLocked(false);
        allRooms.add(openRoom);

        when(roomRepository.findAll()).thenReturn(allRooms);

        assertEquals(allRooms, receptionService.getAvailableRooms());
    }

    @Test
    public void testAvailableRooms_oneOpenOneClosedRoom() {
        List<RoomEntity> allRooms = new ArrayList<>();
        List<RoomEntity> openRooms = new ArrayList<>();

        RoomEntity openRoom = new RoomEntity();
        openRoom.setIsLocked(false);
        allRooms.add(openRoom);
        openRooms.add(openRoom);

        RoomEntity closeRoom = new RoomEntity();
        ;
        closeRoom.setIsLocked(true);
        allRooms.add(closeRoom);

        when(roomRepository.findAll()).thenReturn(allRooms);

        assertEquals(openRooms, receptionService.getAvailableRooms());
    }

    @Test
    public void testAvailableRooms_allClosedRooms() {
        List<RoomEntity> allRooms = new ArrayList<>();

        RoomEntity closeRoom1 = new RoomEntity();
        closeRoom1.setIsLocked(true);
        allRooms.add(closeRoom1);

        RoomEntity closeRoom2 = new RoomEntity();
        ;
        closeRoom2.setIsLocked(true);
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
        ReservationEntity expectedToSaveReservation = ReservationEntity.builder()
                .withReservationDate(simulatedDate)
                .withCustomerId(simulatedCustomerId).build();

        ReservationEntity simulatedSavedReservation = ReservationEntity.builder()
                .withCustomerId(simulatedCustomerId)
                .withReservationDate(simulatedDate)
                .withReservationId(simulatedReservationId).build();

        ReservationRoomEntity expectedReservationRoom1 =
                createExpectedReservationRoom(simulatedReservationId, simulatedRoomsIds.get(0));
        ReservationRoomEntity expectedReservationRoom2 =
                createExpectedReservationRoom(simulatedReservationId, simulatedRoomsIds.get(1));

        RoomEntity simulatedRoom1 =
                createRoom(simulatedRoomsIds.get(0), "2", 5, false);
        RoomEntity expectedToSaveRoom1 =
                createRoom(simulatedRoomsIds.get(0), "2", 5, true);

        RoomEntity simulatedRoom2 =
                createRoom(simulatedRoomsIds.get(1), "3", 4, false);
        RoomEntity expectedToSaveRoom2 =
                createRoom(simulatedRoomsIds.get(1), "3", 4, true);

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

        RoomEntity simulatedRoom1 = createRoom(1L, "1", 5, true);
        RoomEntity simulatedRoom2 = createRoom(2L, "2", 10, true);


        when(reservationRoomRepository.findAll()).thenReturn(reservationRooms);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(simulatedRoom1));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(simulatedRoom2));

        receptionService.deleteReservation(simulatedReservationId);

        verify(reservationRoomRepository).deleteById(100L);
        verify(reservationRoomRepository).deleteById(200L);
        verify(reservationRepository).deleteById(4L);
        verify(roomRepository).save(createRoom(1L, "1", 5, false));
        verify(roomRepository).save(createRoom(2L, "2", 10, false));
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

    private RoomEntity createRoom(Long roomId, String roomNumber,
                                  Integer maxPersons, Boolean isLock) {
        RoomEntity simulateRoom = new RoomEntity();
        simulateRoom.setRoomId(roomId);
        simulateRoom.setRoomNumber(roomNumber);
        simulateRoom.setMaxPersons(maxPersons);
        simulateRoom.setIsLocked(isLock);
        return simulateRoom;
    }

}
