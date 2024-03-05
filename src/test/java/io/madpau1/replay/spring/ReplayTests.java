package io.madpau1.replay.spring;

import io.madpau1.replay.Event;
import io.madpau1.replay.EventPlayer;
import io.madpau1.replay.EventsAdapter;
import io.madpau1.replay.spring.srv.entity.Event0;
import io.madpau1.replay.spring.srv.entity.Event1;
import io.madpau1.replay.spring.srv.entity.Event2;
import io.madpau1.replay.spring.srv.SrvHandlers;
import io.madpau1.replay.spring.srv.Srv;
import java.util.List;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReplayTests {

    @MockBean Srv remoteControl;
    @Autowired ApplicationEventPublisher eventPublisher;
    @Autowired EventPlayer eventPlayer;
    @Autowired EventsAdapter events;

    @Test
    @Order(1)
    public void testHandle() {

        MDC.put(Event0.CORRELATION_ID, "ABC");

        eventPublisher.publishEvent(new Event1("press A"));
        eventPublisher.publishEvent(new Event2("press B"));

        TestTransaction.flagForCommit();
        TestTransaction.end();

        Mockito.verify(remoteControl).handle(Mockito.eq("press A"));
        Mockito.verify(remoteControl).handle(Mockito.eq("press B"));
    }

    @Test
    @Order(1)
    public void testReplay() {
        eventPlayer.replayAll("ABC");

        TestTransaction.flagForCommit();
        TestTransaction.end();

        Mockito.verify(remoteControl).handle(Mockito.eq("press A"));
        Mockito.verify(remoteControl).handle(Mockito.eq("press B"));
    }

    @Test
    @Order(2)
    public void testReplaySingle() {

        List<Event> eventList = events.findAllEventsByMdcId("ABC");
        Assertions.assertEquals(2, eventList.size());

        String eventId = eventList.stream().filter(e -> e.getName().equals(Event1.class.getSimpleName()))
                .map(Event::getId).findFirst()
                .orElseThrow();
        eventPlayer.replay(eventId);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        Mockito.verify(remoteControl).handle(Mockito.eq("press A"));
        Mockito.verify(remoteControl, Mockito.never()).handle(Mockito.eq("press B"));
    }

    @TestConfiguration
    public static class Config {

        @Bean
        SrvHandlers handlers(Srv remoteControl) {
            return new SrvHandlers(remoteControl);
        }

        @Bean
        EventPlayer eventPlayer(ApplicationEventPublisher eventPublisher, EventsAdapter events) {
            return new EventPlayerImpl(eventPublisher, events);
        }

        @Bean
        EventStorage eventStorage(EventsAdapter events) {
            return new EventStorage(events);
        }
    }

}
