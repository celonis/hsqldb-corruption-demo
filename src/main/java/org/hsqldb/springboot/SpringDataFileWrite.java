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
        final var originalValue = readExampleToString();
        entity.setCorruptedValue(originalValue);
        entity.setExpectedLength(originalValue.length());

        final var saved = this.repository.save(entity);
        System.out.printf("[%s] Saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s, corruptedValue=%s%n",
            Thread.currentThread().getName(),
            originalValue.length(), saved.getExpectedLength(), saved.getCorruptedValue().length(),
            saved.getId(),
            concatString(saved.getCorruptedValue()));

        final var sleepTime = 1000;
        System.out.printf("%n[%s] Wait %d seconds before reading%n", Thread.currentThread().getName(), sleepTime / 1000);
        Thread.sleep(sleepTime);

        System.out.printf("[%s] Reading%n", Thread.currentThread().getName());
        final var lastFetched = repository.findTopByOrderByIdDesc();
        System.out.printf("[%s] Fetched last saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s, corruptedValue=%s%n",
            Thread.currentThread().getName(),
            originalValue.length(), lastFetched.getExpectedLength(), lastFetched.getCorruptedValue().length(),
            lastFetched.getId(), concatString(lastFetched.getCorruptedValue()));

        if (lastFetched.getCorruptedValue().length() != originalValue.length()) {
            System.out.printf("%n!!!!!!%nERROR: original string length is %d, but fetched length is %d %n!!!!!!%n%n",
                originalValue.length(), lastFetched.getCorruptedValue().length());
        }
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void read() throws InterruptedException {
//        System.out.printf("[%s] Reading%n", Thread.currentThread().getName());
//        Thread.sleep(1000);
//        final var lastSaved = repository.findTopByOrderByIdDesc();
//        System.out.printf("[%s] Fetched last saved entity: realLength=%d, expectedLength=%d, id=%s, corruptedValue=%s%n",
//            Thread.currentThread().getName(),
//            lastSaved.getCorruptedValue().length(), lastSaved.getExpectedLength(),
//            lastSaved.getId(),
//            concatString(lastSaved.getCorruptedValue()));
//    }
}