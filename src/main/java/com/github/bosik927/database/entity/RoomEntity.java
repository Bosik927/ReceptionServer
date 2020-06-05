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
@Table(name = "rooms")
public class RoomEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "max_persons")
    private Integer maxPersons;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "is_locked")
    private Boolean isLocked;

    private RoomEntity(Long roomId, Integer maxPersons, String roomNumber, Boolean isLocked) {
        this.roomId = roomId;
        this.maxPersons = maxPersons;
        this.roomNumber = roomNumber;
        this.isLocked = isLocked;
    }

    protected RoomEntity() {
    }

    public Long getRoomId() {
        return this.roomId;
    }

    public Integer getMaxPersons() {
        return this.maxPersons;
    }

    public String getRoomNumber() {
        return this.roomNumber;
    }

    public Boolean getIsLocked() {
        return this.isLocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomEntity)) return false;
        RoomEntity that = (RoomEntity) o;
        return Objects.equals(getRoomId(), that.getRoomId()) &&
                Objects.equals(getMaxPersons(), that.getMaxPersons()) &&
                Objects.equals(getRoomNumber(), that.getRoomNumber()) &&
                Objects.equals(getIsLocked(), that.getIsLocked());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomId(), getMaxPersons(), getRoomNumber(), getIsLocked());
    }

    public static Builder builder() {
        return new Builder();
    }

    static public class Builder {
        private Long roomId;
        private Integer maxPersons;
        private String roomNumber;
        private Boolean isLocked;

        public Builder withRoomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder withMaxPersons(Integer maxPersons) {
            this.maxPersons = maxPersons;
            return this;
        }

        public Builder withRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public Builder isLocked(Boolean locked) {
            isLocked = locked;
            return this;
        }

        public RoomEntity build() {
            return new RoomEntity(roomId, maxPersons, roomNumber, isLocked);
        }
    }
}
