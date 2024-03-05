package io.madpau1.replay.spring.srv;

import io.madpau1.replay.spring.srv.entity.Event1;
import io.madpau1.replay.spring.srv.entity.Event2;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
public class SrvHandlers {

    private final Srv remoteControl;

    @TransactionalEventListener
    public void handle1(Event1 event1) {
        remoteControl.handle(event1.getData1());
    }

    @TransactionalEventListener
    public void handle2(Event2 event2) {
        remoteControl.handle(event2.getData2());
    }

}
