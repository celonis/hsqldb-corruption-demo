package org.hsqldb.corruption;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hsqldb.jdbc.JDBCClobClient;

public class StringExtractor implements ResultSetExtractor<String>{

    private final String columnName;

    public StringExtractor(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String extractData(ResultSet rs) throws SQLException {
        rs.next();
        final var clobClient = (JDBCClobClient) rs.getObject(columnName);
        final var charBuf = new char[1024];
        final var stringBuf = new StringBuilder(charBuf.length);
        final var bufferedReader = new BufferedReader(clobClient.getCharacterStream());
        while (true) {
            final int read;
            try {
                read = bufferedReader.read(charBuf);
            } catch (final IOException e) {
                throw new IllegalStateException(e);
            }
            if (read >= 0) {
                stringBuf.append(charBuf, 0, read);
            } else {
                break;
            }
        }

        return stringBuf.toString();
    }
}
