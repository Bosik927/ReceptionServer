package com.github.bosik927.notification.control;

import com.github.bosik927.database.boundary.NotificationRepository;
import com.github.bosik927.database.boundary.ReservationRepository;
import com.github.bosik927.database.entity.NotificationEntity;

import com.github.bosik927.database.entity.ReservationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Predicate;

import static com.github.bosik927.notification.entity.Constants.*;

@Service
public class Reminder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reminder.class);

    private final NotificationRepository notificationRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public Reminder(NotificationRepository notificationRepository,
                    ReservationRepository reservationRepository) {
        this.notificationRepository = notificationRepository;
        this.reservationRepository = reservationRepository;
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void send() {
        Date dateToRemind = Date.valueOf(LocalDate.now().plusDays(1));

        LOGGER.info(REMINDER_LOG, STARTING, dateToRemind);

        reservationRepository.findAll().stream()
                .filter(toRemind(dateToRemind))
                .forEach(reservationEntity ->
                        insertNotification(reservationEntity.getCustomerId(),
                                crateNotificationContent(dateToRemind)));

        LOGGER.info(REMINDER_LOG, ENDING, dateToRemind);
    }

    private Predicate<ReservationEntity> toRemind(Date dateToRemind) {
        return reservationEntity -> reservationEntity.getReservationDate()
                .equals(dateToRemind);
    }


    private void insertNotification(Long accountId, String content) {
        NotificationEntity notificationEntity = NotificationEntity.build()
                .withAccountId(accountId)
                .withNotificationContent(content)
                .isRead(false)
                .build();

        notificationRepository.save(notificationEntity);
    }

    private String crateNotificationContent(Date dateToRemind) {
        return NOTIFICATION_PATTERN + dateToRemind;
    }
}
