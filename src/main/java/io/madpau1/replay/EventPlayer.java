package io.madpau1.replay;

public interface EventPlayer {
    void replay(String eventId);
    void replayAll(String mdcId);
}
