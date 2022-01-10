package org.hsqldb.corruption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.hsqldb.jdbc.JDBCClob;

public class DbUtil {

    private static final Connection conn;

    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");

            conn = DriverManager.getConnection("jdbc:hsqldb:file:"
                    + "test-corruption/db_file"
                    + ";crypt_type=blowfish;crypt_key=1234567890abcdef1234567890abcdef;crypt_lobs=true"
//                    + ";hsqldb.write_delay=0"
                ,
                "sa", "password");
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void shutdown() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("SHUTDOWN");
        conn.close();
    }

    //use for SQL command SELECT
    public static synchronized <T> T query(String expression, ResultSetExtractor<T> resultSetExtractor) throws SQLException {
        try (var st = conn.createStatement()) {
            return resultSetExtractor.extractData(st.executeQuery(expression));
        }
    }

    public static synchronized void query(String expression) throws SQLException {
        query(expression, ignore -> null);
    }

    //use for SQL commands CREATE, DROP, INSERT and UPDATE
    public static synchronized void update(String expression) throws SQLException {
        Statement st = conn.createStatement();
        int i = st.executeUpdate(expression);
        if (i == -1) {
            System.out.println("db error : " + expression);
        }
        st.close();
    }

    //use for prepared SQL commands CREATE, DROP, INSERT and UPDATE with clob field
    public static synchronized void updateClob(String expression, String value) throws SQLException {
        final var st = conn.prepareStatement(expression);
        st.setClob(1, new JDBCClob(value));
        final var i = st.executeUpdate();
        if (i < 0) {
            System.out.println("db error : " + expression);
        }
        st.close();
    }
}
