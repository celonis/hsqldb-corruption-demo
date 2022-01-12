package org.hsqldb.springboot;

import static org.hsqldb.util.LobUtil.concatString;
import static org.hsqldb.util.LobUtil.readExampleToString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringDataFileWrite {

    private final EntityCorruptedRepository repository;

    @Autowired
    public SpringDataFileWrite(EntityCorruptedRepository repository) {
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void write() throws InterruptedException {
        final var entity = new EntityCorrupted();
        entity.setCorruptedValue(readExampleToString());

        final var saved = this.repository.save(entity);
        System.out.printf("Saved entity id=%s, corruptedValue=%s%n", saved.getId(), concatString(saved.getCorruptedValue()));

        final var sleepTime = 5000;
        System.out.printf("%nWait %d seconds before shut down%n", sleepTime / 1000);
        Thread.sleep(sleepTime);
    }

//    @Scheduled
//    public void
}
