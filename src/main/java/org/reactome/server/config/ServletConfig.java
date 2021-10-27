package org.reactome.server.config;

import org.reactome.server.utils.proxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@PropertySource("classpath:disease.properties")
public class ServletConfig {

    @Value("${proxy.host}")
    private String proxyHost;

    @Bean
    public ServletRegistrationBean<ProxyServlet> disgenetServletBean() {
        ServletRegistrationBean<ProxyServlet> bean = new ServletRegistrationBean<>(new ProxyServlet(), "/overlays/*");
        bean.setName("Disgenet");
        bean.setInitParameters(Map.of(
                "proxyHost", "localhost",
                "proxyPort", "8080",
                "proxyPath", "/",
                "proxyProtocol", "http")
        );
        return bean;
    }

    @Bean
    public ServletRegistrationBean<ProxyServlet> pluginsServletBean() {
        ServletRegistrationBean<ProxyServlet> bean = new ServletRegistrationBean<>(new ProxyServlet(), "/plugins/*");
        bean.setName("Plugins");
        bean.setInitParameters(Map.of(
                "proxyHost", this.proxyHost,
                "proxyPort", "80",
                "proxyPath", "/plugins")
        );
        return bean;
    }

    @Bean
    public ServletRegistrationBean<ProxyServlet> mediaServletBean() {
        ServletRegistrationBean<ProxyServlet> bean = new ServletRegistrationBean<>(new ProxyServlet(), "/media/*");
        bean.setName("Media");
        bean.setInitParameters(Map.of(
                "proxyHost", this.proxyHost,
                "proxyPort", "80",
                "proxyPath", "/media")
        );
        return bean;
    }

    @Bean
    public ServletRegistrationBean<ProxyServlet> templatesServletBean() {
        ServletRegistrationBean<ProxyServlet> bean = new ServletRegistrationBean<>(new ProxyServlet(), "/templates/*");
        bean.setName("Templates");
        bean.setInitParameters(Map.of(
                "proxyHost", this.proxyHost,
                "proxyPort", "80",
                "proxyPath", "/templates")
        );
        return bean;
    }

    @Bean
    public ServletRegistrationBean<ProxyServlet> modulesServletBean() {
        ServletRegistrationBean<ProxyServlet> bean = new ServletRegistrationBean<>(new ProxyServlet(), "/modules/*");
        bean.setName("Modules");
        bean.setInitParameters(Map.of(
                "proxyHost", this.proxyHost,
                "proxyPort", "80",
                "proxyPath", "/modules")
        );
        return bean;
    }
}
