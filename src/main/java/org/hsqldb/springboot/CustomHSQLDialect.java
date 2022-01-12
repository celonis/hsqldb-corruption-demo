package org.hsqldb.springboot;

import java.sql.Types;

public class CustomHSQLDialect extends org.hibernate.dialect.HSQLDialect {

    public CustomHSQLDialect() {
        super();
        registerColumnType(Types.CLOB, "clob");
        registerColumnType(Types.BLOB, "blob");
    }
}
