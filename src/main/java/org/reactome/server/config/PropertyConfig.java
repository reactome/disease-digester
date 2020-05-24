package org.reactome.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:application.properties")
public class PropertyConfig {
    @Value("${dataSource.driverClassName}")
    private String driver;
    @Value("${dataSource.url}")
    private String url;
    @Value("${dataSource.username}")
    private String username;
    @Value("${dataSource.password}")
    private String password;
    @Value("${mybatis.type.aliases.package}")
    private String typeAliasesPackage;

    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;
    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.format_sql}")
    private String formatSql;
    @Value("${hibernate.use_sql_comments}")
    private String useSqlComments;
    @Value("${hibernate.use_second_level_cache}")

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public String getDialect() {
        return dialect;
    }

    public String getHbm2ddlAuto() {
        return hbm2ddlAuto;
    }

    public String getShowSql() {
        return showSql;
    }

    public String getFormatSql() {
        return formatSql;
    }

    public String getUseSqlComments() {
        return useSqlComments;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer preferencesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
