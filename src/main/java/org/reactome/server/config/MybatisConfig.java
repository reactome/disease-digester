package org.reactome.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan("org.reactome.server.mapper")
public class MybatisConfig {
    private final PropertyConfig propertyConfig;

    public MybatisConfig(PropertyConfig propertyConfig) {
        this.propertyConfig = propertyConfig;
    }

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(propertyConfig.getUsername());
        dataSource.setPassword(propertyConfig.getPassword());
        dataSource.setUrl(propertyConfig.getUrl());
        dataSource.setDriverClassName(propertyConfig.getDriver());
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(this.dataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage(propertyConfig.getTypeAliasesPackage());
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        sqlSessionFactory.getConfiguration().setCacheEnabled(true);
        return sqlSessionFactory;
    }

    @Bean
    public DataSourceTransactionManager DataSourceTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }
}
