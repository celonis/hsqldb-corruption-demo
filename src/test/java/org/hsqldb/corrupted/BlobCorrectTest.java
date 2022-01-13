package org.hsqldb.corrupted;

import static java.lang.String.format;
import static org.hsqldb.util.LobUtil.generateBlob;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hsqldb.corrupted.model.BlobCorrect;
import org.hsqldb.corrupted.model.BlobCorrectRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * All BLOB tests are success
 * @see CorruptedDataSavedExample
 */
@SpringBootTest(classes = DbCorruptionAppTestConfig.class)
class BlobCorrectTest {

    @Autowired
    private BlobCorrectRepository repository;

    @ParameterizedTest
    @ValueSource(ints = {
        512 * 1024, // 512 KiB
        1024 * 1024 // 1 MiB
    })
    void testBlob(int blobLength) throws InterruptedException {
        final var entity = new BlobCorrect();
        final var originalValue = generateBlob(blobLength); // 1MiB
        entity.setBlobValue(originalValue);
        entity.setExpectedLength(originalValue.length);

        final var saved = this.repository.save(entity);
        System.out.printf("[%s] Saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s%n",
            Thread.currentThread().getName(),
            originalValue.length, saved.getExpectedLength(), saved.getBlobValue().length,
            saved.getId());

        final var sleepTime = 1000;
        System.out.printf("%n[%s] Wait %d seconds before reading%n", Thread.currentThread().getName(), sleepTime / 1000);
        Thread.sleep(sleepTime);

        System.out.printf("[%s] Reading%n", Thread.currentThread().getName());
        final var fetched = repository.findById(saved.getId()).orElseThrow();
        System.out.printf("[%s] Fetched last saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s%n",
            Thread.currentThread().getName(),
            originalValue.length, fetched.getExpectedLength(), fetched.getBlobValue().length,
            fetched.getId());

        assertEquals(originalValue.length, fetched.getBlobValue().length,
            () -> format("%n!!!!!!%nERROR: original string length is %d, but fetched length is %d %n!!!!!!%n%n",
                originalValue.length, fetched.getBlobValue().length));
    }
}