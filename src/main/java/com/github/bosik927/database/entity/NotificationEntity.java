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
@Table(name = "notifications")
public class NotificationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "notification_content")
    private String notificationContent;

    @Column(name = "is_read")
    private Boolean isRead;

    protected NotificationEntity() {
    }

    private NotificationEntity(Long notificationId, Long accountId,
                               String notificationContent, Boolean isRead){
        this.notificationId = notificationId;
        this.accountId = accountId;
        this.notificationContent = notificationContent;
        this.isRead = isRead;
    }

    public Long getNotificationId() {
        return this.notificationId;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public String getNotificationContent() {
        return this.notificationContent;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationEntity)) return false;
        NotificationEntity that = (NotificationEntity) o;
        return Objects.equals(getNotificationId(), that.getNotificationId()) &&
                Objects.equals(getAccountId(), that.getAccountId()) &&
                Objects.equals(getNotificationContent(), that.getNotificationContent()) &&
                Objects.equals(getIsRead(), that.getIsRead());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNotificationId(), getAccountId(), getNotificationContent(), getIsRead());
    }

    public static NotificationEntity.Builder build(){
        return new Builder();
    }

    public static class Builder{
        private Long notificationId;
        private Long accountId;
        private String notificationContent;
        private Boolean isRead;

        public Builder withNotificationId(Long notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public Builder withAccountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder withNotificationContent(String notificationContent) {
            this.notificationContent = notificationContent;
            return this;
        }

        public Builder isRead(Boolean read) {
            isRead = read;
            return this;
        }

        public NotificationEntity build(){
            return new NotificationEntity(notificationId,accountId, notificationContent, isRead);
        }
    }
}
