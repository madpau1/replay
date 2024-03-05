package io.madpau1.replay.spring;

import io.madpau1.replay.EventPlayer;
import io.madpau1.replay.EventsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class EventPlayerImpl implements EventPlayer {

    private final ApplicationEventPublisher eventPublisher;

    private final EventsAdapter events;

    @Override
    @Transactional
    public void replay(String eventId) {
        events.findEventById(eventId).ifPresent(eventPublisher::publishEvent);
    }

    @Override
    @Transactional
    public void replayAll(String mdcId) {
        events.findAllEventsByMdcId(mdcId).forEach(eventPublisher::publishEvent);
    }

}
