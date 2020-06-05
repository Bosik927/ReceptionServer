package com.github.bosik927.database.boundary;

import com.github.bosik927.database.entity.ReservationRoomEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRoomRepository extends JpaRepository<ReservationRoomEntity, Long> {
}
