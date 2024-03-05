package io.madpau1.replay;

import java.util.List;
import java.util.Optional;

public interface EventsAdapter<E extends Event> {
    void saveEvent(E event);
    Optional<E> findEventById(String id);
    List<E> findAllEventsByMdcId(String id);
    List<E> findAllEventsByContext(String context);
}
