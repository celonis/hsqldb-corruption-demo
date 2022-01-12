package org.hsqldb.springboot;

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
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DatabaseConfigurator databaseConfigurator) throws Exception {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(databaseConfigurator.getDataSource());
        entityManagerFactory.setPackagesToScan("org.hsqldb.springboot");
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter(databaseConfigurator));
        entityManagerFactory.setJpaProperties(databaseConfigurator.getJpaProperties());
        return entityManagerFactory;
    }

    private JpaVendorAdapter jpaVendorAdapter(DatabaseConfigurator databaseConfigurator) {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setDatabasePlatform(databaseConfigurator.getDialect());
        return jpaVendorAdapter;
    }
}
