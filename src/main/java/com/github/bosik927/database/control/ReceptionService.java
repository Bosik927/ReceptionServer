package com.github.bosik927.database.control;

import com.github.bosik927.database.boundary.ReservationRepository;
import com.github.bosik927.database.boundary.ReservationRoomRepository;
import com.github.bosik927.database.boundary.RoomRepository;
import com.github.bosik927.database.entity.ReservationDTO;
import com.github.bosik927.database.entity.ReservationEntity;
import com.github.bosik927.database.entity.ReservationRoomEntity;
import com.github.bosik927.database.entity.RoomEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ReceptionService {

    private final RoomRepository roomRepository;
    private final ReservationRoomRepository reservationRoomRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReceptionService(final RoomRepository roomRepository,
                            final ReservationRoomRepository reservationRoomRepository,
                            final ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRoomRepository = reservationRoomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public List<RoomEntity> getAvailableRooms() {
        return roomRepository.findAll()
                .stream()
                .filter(isOpenRoom())
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationEntity createReservation(ReservationDTO reservationDTO) {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setCustomerId(reservationDTO.getCustomer_id());
        reservationEntity.setReservationDate(reservationDTO.getReservationDate());

        ReservationEntity savedEntity = reservationRepository.save(reservationEntity);

        reservationDTO.getRoomsIds()
                .forEach(roomId -> {
                    ReservationRoomEntity entity = new ReservationRoomEntity();
                    entity.setReservationId(savedEntity.getReservationId());
                    entity.setRoomId(roomId);

                    reservationRoomRepository.save(entity);

                    roomRepository.findById(roomId)
                            .map(roomEntity ->
                                    RoomEntity.builder()
                                            .withMaxPersons(roomEntity.getMaxPersons())
                                            .withRoomId(roomEntity.getRoomId())
                                            .withRoomNumber(roomEntity.getRoomNumber())
                                            .isLocked(true)
                                            .build())
                            .ifPresent(roomRepository::save);
                });

        return savedEntity;
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRoomRepository.findAll().stream()
                .filter(toDelete(id))
                .collect(Collectors.toList())
                .forEach(reservationRoom -> {
                            reservationRoomRepository.deleteById(
                                    reservationRoom.getReservationsRoomsId());

                            roomRepository.findById(reservationRoom.getRoomId())
                                    .map(roomEntity ->
                                            RoomEntity.builder()
                                                    .withMaxPersons(roomEntity.getMaxPersons())
                                                    .withRoomId(roomEntity.getRoomId())
                                                    .withRoomNumber(roomEntity.getRoomNumber())
                                                    .isLocked(false)
                                                    .build())
                                    .ifPresent(roomRepository::save);
                        }
                );

        reservationRepository.deleteById(id);
    }

    private Predicate<RoomEntity> isOpenRoom() {
        return room -> !room.getIsLocked();
    }

    private Predicate<ReservationRoomEntity> toDelete(long id) {
        return reservationRoom -> reservationRoom.getReservationId() == id;
    }
}
