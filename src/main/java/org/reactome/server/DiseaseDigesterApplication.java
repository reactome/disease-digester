package org.reactome.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "org.reactome.server")
@EntityScan(basePackages = "org.reactome.server.domain.model")
public class DiseaseDigesterApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DiseaseDigesterApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DiseaseDigesterApplication.class, args);
    }

}
