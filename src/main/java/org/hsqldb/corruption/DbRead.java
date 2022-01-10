package org.hsqldb.corruption;

import java.sql.SQLException;

public class DbRead {

    public static void main(String[] args) throws SQLException {

        final var result = DbUtil.query("SELECT value FROM clob_corruption", new StringExtractor("value"));

        final var resultStringLength = result.length();
        System.out.printf("Result length is %d. Read string is: %s...%s",
            resultStringLength, result.substring(0, 20), result.substring(Math.max(0, resultStringLength - 20), resultStringLength));
    }
}