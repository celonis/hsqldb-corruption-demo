package org.hsqldb.springboot;

import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class PersistenceConfiguration {

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
        jpaVendorAdapter.setDatabasePlatform(getDialect());
        return jpaVendorAdapter;
    }

    private String getDialect() {
        return HSQLDialect.class.getCanonicalName();
    }

    private DataSource getDataSource() {
        final var dataSource = new JDBCDataSource();
        dataSource.setURL("jdbc:hsqldb:file:test-corruption-springboot/db_file;crypt_key=1234567890abcdef1234567890abcdef;crypt_type=blowfish;crypt_lobs=true");
        dataSource.setUser("sa");
        dataSource.setPassword("password");
        return dataSource;
    }
}
