package io.madpau1.replay.spring.srv.entity;

import io.madpau1.replay.Event;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.MDC;

@Getter
@Setter
@Entity
@Inheritance
@Table(name = "replay_events")
public abstract class Event0 implements Event {

    public static final String CORRELATION_ID = "correlation_id";

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @CreationTimestamp
    private Instant createdAt;

    @NotNull
    private String mdcId = MDC.get(CORRELATION_ID);

    @NotNull
    private String name = getClass().getSimpleName();

    private String context;

}
