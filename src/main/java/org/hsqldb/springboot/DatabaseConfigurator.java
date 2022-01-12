package org.hsqldb.springboot;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfigurator {

    private final String driverClassName;
    private final String url;
    private final String dialect;
    private final String username;
    private final String password;
    private final int minPoolSize;
    private final int maxPoolSize;
    private final int maxIdle;
    private final int checkoutTimeout;

    public DatabaseConfigurator(
        @Value("${database.driverClassName:}") String driverClassName,
        @Value("${database.url:}") String url,
        @Value("${database.dialect:}") String dialect,
        @Value("${database.username:}") String username,
        @Value("${database.password:}") String password,
        @Value("${database.minPoolSize:3}") int minPoolSize,
        @Value("${database.maxPoolSize:100}") int maxPoolSize,
        @Value("${database.maxIdle:300}") int maxIdle,
        @Value("${database.checkoutTimeout:100000 }") int checkoutTimeout) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.dialect = dialect;
        this.username = username;
        this.password = password;
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.maxIdle = maxIdle;
        this.checkoutTimeout = checkoutTimeout;
    }

    public String getDriverClassName() {
        return "org.hsqldb.jdbc.JDBCDriver";
    }

    public String getUrl() {
        return "jdbc:hsqldb:file:test-corruption-springboot/db_file;crypt_key=1234567890abcdef1234567890abcdef;crypt_type=blowfish;crypt_lobs=true";
    }

    public String getDialect() {
        return "org.hsqldb.springboot.HSQLDialect";
    }

    public int getCheckoutTimeout() {
        return checkoutTimeout;
    }

    public String getUsername() {
        return "sa";
    }

    public String getPassword() {
        return "password";
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public Properties getJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");

        /* hibernate 5 uses sequences by default for auto generated keys
           since HSQL doesn't support sequences, this reverts back to the old behaviour where hibernate
           uses the method native to each database

           https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-auto
        */
        properties.setProperty("hibernate.id.new_generator_mappings", "false");

        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "false");

        properties.setProperty("hibernate.generate_statistics", "false");
        properties.setProperty("hibernate.c3p0.min_size", "5");
        properties.setProperty("hibernate.c3p0.max_size", "15");
        properties.setProperty("hibernate.c3p0.timeout", "18000");
        properties.setProperty("hibernate.c3p0.checkoutTimeout", "180000");
        properties.setProperty("hibernate.c3p0.max_statements", "100");
        properties.setProperty("hibernate.c3p0.idleTestPeriod", "300");
        properties.setProperty("hibernate.c3p0.testConnectionOnCheckout", "true");

        // cache settings
        properties.setProperty("net.sf.ehcache.configurationResourceName", "ehcache.xml");
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.SingletonEhCacheProvider");
        properties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.use_query_cache", "true");
        properties.setProperty("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");

        properties.setProperty("minPoolSize", "5");
        properties.setProperty("maxPoolSize", "100");

        return properties;
    }


    public DataSource getDataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(getDriverClassName());
        dataSource.setJdbcUrl(getUrl());
        dataSource.setUser(getUsername());
        dataSource.setPassword(getPassword());
        dataSource.setMaxIdleTime(getMaxIdle());
        dataSource.setMaxPoolSize(getMaxPoolSize());
        dataSource.setMinPoolSize(getMinPoolSize());
        dataSource.setCheckoutTimeout(getCheckoutTimeout());
        return dataSource;
    }

}
