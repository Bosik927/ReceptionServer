package com.github.bosik927.notification.control;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.bosik927.database.boundary.NotificationRepository;
import com.github.bosik927.database.boundary.ReservationRepository;
import com.github.bosik927.database.entity.NotificationEntity;
import com.github.bosik927.database.entity.ReservationEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReminderTest {

    @Mock
    NotificationRepository notificationRepository;
    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    Reminder reminder;

    @Test
    public void testSend_TestingRepositoriesInvocations() {
        Date dateToRemind = Date.valueOf(LocalDate.now().plusDays(1));
        Date notRemindDate = Date.valueOf(LocalDate.now());

        List<ReservationEntity> reservations = new ArrayList<>();
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setCustomerId(1L);
        reservationEntity.setReservationDate(dateToRemind);
        reservationEntity.setReservationId(1L);
        reservations.add(reservationEntity);
        ReservationEntity reservationEntity2 = new ReservationEntity();
        reservationEntity2.setCustomerId(2L);
        reservationEntity2.setReservationDate(dateToRemind);
        reservationEntity2.setReservationId(2L);
        reservations.add(reservationEntity2);
        ReservationEntity reservationEntity3 = new ReservationEntity();
        reservationEntity3.setCustomerId(1L);
        reservationEntity3.setReservationDate(notRemindDate);
        reservationEntity3.setReservationId(3L);
        reservations.add(reservationEntity3);

        NotificationEntity expectedEntity1 = NotificationEntity.build()
                .withAccountId(1L)
                .withNotificationContent("Przypominamy o rezerwacji na dzien " + dateToRemind)
                .isRead(false)
                .build();
        NotificationEntity expectedEntity2 = NotificationEntity.build()
                .withAccountId(2L)
                .withNotificationContent("Przypominamy o rezerwacji na dzien " + dateToRemind)
                .isRead(false)
                .build();

        when(reservationRepository.findAll()).thenReturn(reservations);

        reminder.send();

        verify(notificationRepository, times(2)).save(any());
        verify(notificationRepository).save(expectedEntity1);
        verify(notificationRepository).save(expectedEntity2);
    }

}
