package org.hsqldb.corruption;

import java.sql.SQLException;

/**
 * Insert 1MiB alphanumeric string.
 */
public class DbWrite {

    public static final int oneMebiByte = 1024 * 1024; // 1MiB
    public static char[] charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();


    public static void main(String[] args) throws SQLException {

        DbUtil.update("SET FILES WRITE DELAY 0");
        DbUtil.update("SET AUTOCOMMIT TRUE");
        DbUtil.update("DROP TABLE IF EXISTS clob_corruption");
        DbUtil.update("CREATE TABLE clob_corruption ( id INTEGER IDENTITY, value CLOB(1G))");

        final var largeString = new StringBuilder(oneMebiByte + 100);
        largeString.append("begin__");
        for (int i = 0; i < oneMebiByte; i++) {
            largeString.append(charset[i % charset.length]);
        }
        largeString.append("__end");

        DbUtil.update("INSERT INTO clob_corruption(value) VALUES('" + largeString + "')");
        final var largeStringLength = largeString.length();
        System.out.printf("Result length is %d. Writen string is: %s...%s",
            largeStringLength, largeString.substring(0, 20), largeString.substring(Math.max(0, largeStringLength - 20), largeStringLength));

        final var result = DbUtil.query("SELECT value FROM clob_corruption", new StringExtractor("value"));

        final var resultStringLength = result.length();
        System.out.printf("Result length is %d. Read string is: %s...%s",
            resultStringLength, result.substring(0, 20), result.substring(Math.max(0, resultStringLength - 20), resultStringLength));
    }
}