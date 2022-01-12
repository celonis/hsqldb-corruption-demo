package org.hsqldb.springboot;

import java.sql.Types;

public class HSQLDialect extends org.hibernate.dialect.HSQLDialect {

    public HSQLDialect() {
        super();
        registerColumnType(Types.CLOB, "clob");
        registerColumnType(Types.BLOB, "blob");
    }
}
