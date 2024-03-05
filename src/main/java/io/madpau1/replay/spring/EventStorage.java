package io.madpau1.replay.spring;

import io.madpau1.replay.Event;
import io.madpau1.replay.EventsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
class EventStorage {

    private final EventsAdapter events;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(Event event) {
        if (event.getId() == null) {
            events.saveEvent(event);
        }
    }
}
