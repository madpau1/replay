package io.madpau1.replay;

import java.time.Instant;

public interface Event {
    String getId();
    String getName();
    Instant getCreatedAt();
    String getMdcId();
    String getContext();
}
