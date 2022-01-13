package org.hsqldb.corrupted;

import static java.lang.String.format;
import static org.hsqldb.util.LobUtil.concatString;
import static org.hsqldb.util.LobUtil.generateString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import org.hsqldb.corrupted.DbCorruptionAppTest.DbCorruptionAppTestConfig;
import org.hsqldb.corrupted.model.EntityCorrupted;
import org.hsqldb.corrupted.model.EntityCorruptedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

@SpringBootTest(classes = DbCorruptionAppTestConfig.class)
class DbCorruptionAppTest {

    @Autowired
    private EntityCorruptedRepository repository;

    /**
     * @see CorruptedDataSavedExample
     */
    @Test
    void testLobCorruption() throws InterruptedException {
        final var entity = new EntityCorrupted();
        final var originalValue = generateString(1024 * 1024); // 1MiB
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

    /**
     * Exclude {@link CorruptedDataSavedExample} from the test context configuration. Include only persistence config.
     */
    @ComponentScan(basePackageClasses = DbCorruptionApp.class,
        excludeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = CorruptedDataSavedExample.class))
    static class DbCorruptionAppTestConfig {

    }
}