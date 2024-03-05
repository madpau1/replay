package io.madpau1.replay.spring.srv;

import io.madpau1.replay.EventsAdapter;
import io.madpau1.replay.spring.srv.entity.Event0;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepo extends JpaRepository<Event0, String>, EventsAdapter<Event0> {

    @Override
    default void saveEvent(Event0 event) {
        save(event);
    }

    @Override
    default Optional<Event0> findEventById(String id) {
        return findById(id);
    }

    @Query("from Event0 where mdcId = :mdc_id")
    List<Event0> findAllEventsByMdcId(@Param("mdc_id") String id);

    @Query("from Event0 where context = :context")
    List<Event0> findAllEventsByContext(@Param("context") String context);
}
