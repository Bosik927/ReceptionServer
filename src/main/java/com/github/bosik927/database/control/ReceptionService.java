package com.github.bosik927.database.control;

import com.github.bosik927.database.boundary.ReservationRepository;
import com.github.bosik927.database.boundary.ReservationRoomRepository;
import com.github.bosik927.database.boundary.RoomRepository;
import com.github.bosik927.database.entity.ReservationDTO;
import com.github.bosik927.database.entity.ReservationEntity;
import com.github.bosik927.database.entity.ReservationRoomEntity;
import com.github.bosik927.database.entity.RoomEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ReceptionService {

    //    TODO: Use it
    static Logger LOGGER = LoggerFactory.getLogger(ReceptionService.class);

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
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .withCustomerId(reservationDTO.getCustomer_id())
                .withReservationDate(reservationDTO.getReservationDate()).build();

        ReservationEntity savedEntity = reservationRepository.save(reservationEntity);

        reservationDTO.getRoomsIds()
                .forEach(roomId -> {
                    ReservationRoomEntity entity = new ReservationRoomEntity();
                    entity.setReservationId(savedEntity.getReservationId());
                    entity.setRoomId(roomId);

                    reservationRoomRepository.save(entity);

                    roomRepository.findById(roomId)
                            .map(roomEntity -> RoomEntity.builder()
                                    .withRoomId(roomEntity.getRoomId())
                                    .withRoomNumber(roomEntity.getRoomNumber())
                                    .withMaxPersons(roomEntity.getMaxPersons())
                                    .isLocked(true).build())
                            .ifPresent(roomRepository::save);
                });

        return savedEntity;
    }

    @Transactional
//    TODO: Refactor - Bosik927
    public void deleteReservation(Long id) {
        reservationRoomRepository.findAll().stream()
                .filter(toDelete(id))
                .collect(Collectors.toList())
                .forEach(reservationRoom -> {
                            reservationRoomRepository.deleteById(
                                    reservationRoom.getReservationsRoomsId());

//                          TODO: Check
                            roomRepository.findById(reservationRoom.getRoomId()).
                                    map(roomEntity -> RoomEntity.builder()
                                            .withRoomId(roomEntity.getRoomId())
                                            .withRoomNumber(roomEntity.getRoomNumber())
                                            .withMaxPersons(roomEntity.getMaxPersons())
                                            .isLocked(false).build())
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
