package org.reactome.server.config;

import com.mysql.cj.xdevapi.DatabaseObject;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.reflections.Reflections;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi createDisgenetApi(ServletContext context) {
        Reflections reflections = new Reflections(DatabaseObject.class.getPackage().getName());
        for (Class<?> clazz : reflections.getSubTypesOf(DatabaseObject.class)) {
            SpringDocUtils.getConfig().replaceWithClass(clazz, Void.class);
        }

        return GroupedOpenApi.builder()
                .group("disgenet")
                .pathsToMatch("/disgenet/**")
                .addOpenApiCustomiser(
                        openApi -> openApi
                                .addServersItem(new Server().url(context.getContextPath()))
                                .info(new Info()
                                        .title("DisGeNET Analysis Service")
                                        .description("Provides an API for DisGeNET analysis by a specific disease id.<br>" +
                                                "<a href=\"http://www.disgenet.org/\" target=\"_blank\">DisGeNET</a> is a database of gene-disease associations. " +
                                                "The Reactome pathway analysis for a given DisGeNET disease id is a two step process.<br/>" +
                                                "For a DisGeNET disease id, /analyze/ return a JSON object, which in the \"url\" parameter contains the URL that is the direct link to the results visualisation.<br/>" +
                                                "This URL will remain valid for at least seven days, but will eventually expire.")
                                        .version("0.1")
                                        .license(new License()
                                                .name("Creative Commons Attribution 3.0 Unsupported License")
                                                .url("https://creativecommons.org/licenses/by/3.0/legalcode")
                                        )
                                        .termsOfService("/license")
                                        .contact(new Contact()
                                                .name("Reactome")
                                                .email("help@reactome.org")
                                                .url("https://reactome.org")
                                        )
                                ))
                .build();
    }

    @Bean
    public GroupedOpenApi createOpenTargetApi(ServletContext context) {
        Reflections reflections = new Reflections(DatabaseObject.class.getPackage().getName());
        for (Class<?> clazz : reflections.getSubTypesOf(DatabaseObject.class)) {
            SpringDocUtils.getConfig().replaceWithClass(clazz, Void.class);
        }

        return GroupedOpenApi.builder()
                .group("open-target")
                .pathsToMatch("/open-target/**")
                .addOpenApiCustomiser(
                        openApi -> openApi
                                .addServersItem(new Server().url(context.getContextPath()))
                                .info(new Info()
                                        .title("OpenTarget Analysis Service")
                                        .description("Provides an API for Open Target analysis by a specific disease id.<br>" +
                                                "<a href=\"https://www.opentargets.org/\" target=\"_blank\">OpenTarget</a>" +
                                                " (<a href=\"https://pubmed.ncbi.nlm.nih.gov/27899665/\" target=\"_blank\">Koscielny G, et al, Nucleic Acids Res." +
                                                " 2016</a>) is a platform for therapeutic target identification and validation.<br>" +
                                                "The Reactome pathway analysis for a given OpenTarget disease id is a two step process.<br/>" +
                                                "For an OpenTarget disease id, /analyze/ return a JSON object, which in the \"url\" parameter contains the URL that is the direct link to the results visualisation.<br/>" +
                                                "This URL will remain valid for at least seven days, but will eventually expire.")
                                        .version("0.1")
                                        .license(new License()
                                                .name("Creative Commons Attribution 3.0 Unsupported License")
                                                .url("https://creativecommons.org/licenses/by/3.0/legalcode")
                                        )
                                        .termsOfService("/license")
                                        .contact(new Contact()
                                                .name("Reactome")
                                                .email("help@reactome.org")
                                                .url("https://reactome.org")
                                        )
                                ))
                .build();
    }
}