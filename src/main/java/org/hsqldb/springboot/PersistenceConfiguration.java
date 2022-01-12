package org.hsqldb.springboot;

import java.util.Map;
import javax.sql.DataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class PersistenceConfiguration {

    public static final String DB_FILE = "test-corruption-springboot/db_file";

    public static final String CRYPT_CONFIG = ";crypt_key=11111111111111111111111111111111;crypt_type=blowfish;crypt_lobs=true";
    public static final String HSQL_DIALECT = CustomHSQLDialect.class.getCanonicalName();

    private DataSource getDataSource() {
        final var dataSource = new JDBCDataSource();
        dataSource.setURL("jdbc:hsqldb:file:"
            + DB_FILE
            + CRYPT_CONFIG
        );
        dataSource.setUser("sa");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(getDataSource());
        entityManagerFactory.setPackagesToScan("org.hsqldb.springboot");
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());

        entityManagerFactory.setJpaPropertyMap(Map.of("hibernate.hbm2ddl.auto", "update"));

        return entityManagerFactory;
    }

    private JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setDatabasePlatform(HSQL_DIALECT);
        return jpaVendorAdapter;
    }

}
