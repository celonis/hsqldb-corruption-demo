package org.hsqldb.util;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class LobUtil {

    public static String generateString(final int length) {
        final var res = new char[length];
        char c = 'a';
        for (int i = 0; i < length; i++) {
            res[i] = c++;
            if (c >= 'z') {
                c = 'a';
            }
        }

        return new String(res);
    }

    /**
     * Cut middle of the string.
     */
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
