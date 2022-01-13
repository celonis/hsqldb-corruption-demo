package org.hsqldb.springboot;

import static java.lang.String.format;
import static org.hsqldb.util.LobUtil.concatString;
import static org.hsqldb.util.LobUtil.readExampleToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DbCorruptionAppTest {

    @Autowired
    private EntityCorruptedRepository repository;

    @Test
    void testLobCorruption() throws InterruptedException {
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
        final var fetched = repository.findById(saved.getId()).orElseThrow();
        System.out.printf("[%s] Fetched last saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s, corruptedValue=%s%n",
            Thread.currentThread().getName(),
            originalValue.length(), fetched.getExpectedLength(), fetched.getCorruptedValue().length(),
            fetched.getId(), concatString(fetched.getCorruptedValue()));

        assertEquals(originalValue.length(), fetched.getCorruptedValue().length(),
            () -> format("%n!!!!!!%nERROR: original string length is %d, but fetched length is %d %n!!!!!!%n%n",
                originalValue.length(), fetched.getCorruptedValue().length()));
    }
}