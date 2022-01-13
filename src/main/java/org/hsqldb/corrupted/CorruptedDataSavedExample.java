package org.hsqldb.corrupted;

import static org.hsqldb.util.LobUtil.concatString;
import static org.hsqldb.util.LobUtil.generateBlob;
import static org.hsqldb.util.LobUtil.generateString;

import org.hsqldb.corrupted.model.BlobCorrect;
import org.hsqldb.corrupted.model.BlobCorrectRepository;
import org.hsqldb.corrupted.model.ClobCorrupted;
import org.hsqldb.corrupted.model.ClobCorruptedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CorruptedDataSavedExample {

    private final ClobCorruptedRepository clobCorruptedRepository;
    private final BlobCorrectRepository blobCorrectRepository;

    @Autowired
    public CorruptedDataSavedExample(ClobCorruptedRepository clobCorruptedRepository, BlobCorrectRepository blobCorrectRepository) {
        this.clobCorruptedRepository = clobCorruptedRepository;
        this.blobCorrectRepository = blobCorrectRepository;
    }

    /**
     * Just an ad-hoc example to test in spring boot application
     */
    @EventListener(ApplicationReadyEvent.class)
    public void write() throws InterruptedException {
        runClobFailed();
        runBlobSuccess();
    }

    private void runClobFailed() throws InterruptedException {
        final var entity = new ClobCorrupted();
        final var originalValue = generateString(0x100000); // 1MiB
        entity.setCorruptedValue(originalValue);
        entity.setExpectedLength(originalValue.length());

        final var saved = this.clobCorruptedRepository.save(entity);
        System.out.printf("[%s] Saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s, corruptedValue=%s%n",
            Thread.currentThread().getName(),
            originalValue.length(), saved.getExpectedLength(), saved.getCorruptedValue().length(),
            saved.getId(),
            concatString(saved.getCorruptedValue()));

        final var sleepTime = 1000;
        System.out.printf("%n[%s] Wait %d seconds before reading%n", Thread.currentThread().getName(), sleepTime / 1000);
        Thread.sleep(sleepTime);

        System.out.printf("[%s] Reading%n", Thread.currentThread().getName());
        final var lastFetched = clobCorruptedRepository.findById(saved.getId()).orElseThrow();
        System.out.printf("[%s] Fetched last saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s, corruptedValue=%s%n",
            Thread.currentThread().getName(),
            originalValue.length(), lastFetched.getExpectedLength(), lastFetched.getCorruptedValue().length(),
            lastFetched.getId(), concatString(lastFetched.getCorruptedValue()));

        if (lastFetched.getCorruptedValue().length() != originalValue.length()) {
            System.out.printf("%n!!!!!!%nERROR: original string length is %d, but fetched length is %d %n!!!!!!%n%n",
                originalValue.length(), lastFetched.getCorruptedValue().length());
        } else  {
            System.out.printf("%nSUCCESS: CLOB Large value persisted successfully%n%n");
        }
    }

    private void runBlobSuccess() throws InterruptedException {
        final var entity = new BlobCorrect();
        final var originalValue = generateBlob(0x100000); // 1MiB
        entity.setBlobValue(originalValue);
        entity.setExpectedLength(originalValue.length);

        final var saved = this.blobCorrectRepository.save(entity);
        System.out.printf("[%s] Saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s%n",
            Thread.currentThread().getName(),
            originalValue.length, saved.getExpectedLength(), saved.getBlobValue().length,
            saved.getId());

        final var sleepTime = 1000;
        System.out.printf("%n[%s] Wait %d seconds before reading%n", Thread.currentThread().getName(), sleepTime / 1000);
        Thread.sleep(sleepTime);

        System.out.printf("[%s] Reading%n", Thread.currentThread().getName());
        final var lastFetched = blobCorrectRepository.findById(saved.getId()).orElseThrow();
        System.out.printf("[%s] Fetched last saved entity: originalLength=%d, expectedLength=%d, savedLength=%d, id=%s%n",
            Thread.currentThread().getName(),
            originalValue.length, lastFetched.getExpectedLength(), lastFetched.getBlobValue().length,
            lastFetched.getId());

        if (lastFetched.getBlobValue().length != originalValue.length) {
            System.out.printf("%n!!!!!!%nERROR: original string length is %d, but fetched length is %d %n!!!!!!%n%n",
                originalValue.length, lastFetched.getBlobValue().length);
        } else  {
            System.out.printf("%nSUCCESS: BLOB Large value persisted successfully%n%n");
        }
    }
}
