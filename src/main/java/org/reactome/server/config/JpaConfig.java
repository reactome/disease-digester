package org.reactome.server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.Environment;
import org.reactome.server.Application;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackageClasses = Application.class)
class JpaConfig {

    private final PropertyConfig propertyConfig;

    public JpaConfig(PropertyConfig propertyConfig) {
        this.propertyConfig = propertyConfig;
    }


    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(propertyConfig.getDriver());
        config.setJdbcUrl(propertyConfig.getUrl());
        config.setUsername(propertyConfig.getUsername());
        config.setPassword(propertyConfig.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        return new HikariDataSource(config);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        String entities = ClassUtils.getPackageName(Application.class);
        String converters = ClassUtils.getPackageName(Jsr310JpaConverters.class);
        entityManagerFactoryBean.setPackagesToScan(entities, converters);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties jpaProperties = new Properties();
        jpaProperties.put(Environment.DIALECT, propertyConfig.getDialect());
        jpaProperties.put(Environment.HBM2DDL_AUTO, propertyConfig.getHbm2ddlAuto());
        jpaProperties.put(Environment.SHOW_SQL, propertyConfig.getShowSql());
        jpaProperties.put(Environment.FORMAT_SQL, propertyConfig.getFormatSql());
        jpaProperties.put(Environment.USE_SQL_COMMENTS, propertyConfig.getUseSqlComments());
        jpaProperties.put(Environment.HBM2DDL_DELIMITER, ";");
//        jpaProperties.put(Environment.USE_SECOND_LEVEL_CACHE, use_second_level_cache);
        System.setProperty("hibernate.dialect.storage_engine", "innodb");
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}