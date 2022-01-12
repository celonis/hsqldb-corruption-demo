package org.hsqldb.util;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.hsqldb.corruption.DbFileWrite;

public final class LobUtil {

    public static String readResourceToString(String resourceName) {
        try {
            return Files.readString(Paths.get(DbFileWrite.class.getClassLoader().getResource(resourceName).toURI()), StandardCharsets.UTF_8);
        } catch (final Exception e) {
            throw new IllegalStateException("Can't read resource \"" + resourceName + "\":", e);
        }
    }

    public static String readExampleToString() {
        return "begin__" + readResourceToString("example.txt") + "__end";
    }

    public static String concatString(String str, int left, int right) {
        final var len = str.length();
        final var leftChars = min(max(0, left), len);
        final var rightChars = max(0, len - min(len, right));
        return str.substring(0, leftChars) + "..." + str.substring(rightChars);
    }

    public static String concatString(String str) {
        return concatString(str, 20, 20);
    }

    private LobUtil() {
    }
}
