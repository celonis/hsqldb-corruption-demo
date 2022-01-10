package org.hsqldb.corruption;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * insert 100 times {@code example.json} content (&gt; 2MB)
 */
public class DbFileWrite {


    public static void main(String[] args) throws SQLException, IOException, URISyntaxException {

        DbUtil.update("SET FILES WRITE DELAY 0");
        DbUtil.update("SET AUTOCOMMIT TRUE");

        DbUtil.update("DROP TABLE IF EXISTS clob_corruption");
        DbUtil.update("CREATE TABLE clob_corruption ( id INTEGER IDENTITY, value CLOB(1G))");

        final var stringFromFile = Files.readString(Paths.get(DbFileWrite.class.getClassLoader().getResource("example.json").toURI()), StandardCharsets.UTF_8);
        final var largeString = "begin__" + stringFromFile + "__end";

        for (int i = 0; i < 100; i++) {
            DbUtil.updateClob("INSERT INTO clob_corruption(value) VALUES( ? )", largeString);
        }

        final var largeStringLength = largeString.length();
        System.out.printf("Result length is %d. Writen string is: %s...%s",
            largeStringLength, largeString.substring(0, 20), largeString.substring(Math.max(0, largeStringLength - 20), largeStringLength));

        final var result = DbUtil.query("SELECT value FROM clob_corruption", new StringExtractor("value"));

        final var resultStringLength = result.length();
        System.out.printf("Result length is %d. Read string is: %s...%s",
            resultStringLength, result.substring(0, 20), result.substring(Math.max(0, resultStringLength - 20), resultStringLength));
    }
}