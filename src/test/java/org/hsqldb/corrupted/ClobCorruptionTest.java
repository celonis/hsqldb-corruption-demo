package org.hsqldb.corrupted;

import static java.lang.String.format;
import static org.hsqldb.util.LobUtil.concatString;
import static org.hsqldb.util.LobUtil.generateString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hsqldb.corrupted.model.ClobCorrupted;
import org.hsqldb.corrupted.model.ClobCorruptedRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @see CorruptedDataSavedExample
 */
@SpringBootTest(classes = DbCorruptionAppTestConfig.class)
class ClobCorruptionTest {

    @Autowired
    private ClobCorruptedRepository repository;

    @ParameterizedTest
    @ValueSource(ints = {
        512 * 1024, // 512 KiB - success
        1024 * 1024, // 1MiB - fails
    })
    void testClob(int stringLength) throws InterruptedException {
        final var entity = new ClobCorrupted();
        final var originalValue = generateString(stringLength);
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