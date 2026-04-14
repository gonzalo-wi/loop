package com.eljumillano.loop.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages            = "com.eljumillano.loop.repository.sqlserver",
    entityManagerFactoryRef = "sqlServerEntityManagerFactory",
    transactionManagerRef   = "sqlServerTransactionManager"
)
public class SqlServerDataSourceConfig {

    @Bean(name = "sqlServerDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlServerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("sqlServerDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.eljumillano.loop.model"); 
        em.setPersistenceUnitName("sqlserver");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.put("hibernate.show_sql", true);
        em.setJpaProperties(properties);
        
        return em;
    }

    
    @Bean(name = "sqlServerTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("sqlServerEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
