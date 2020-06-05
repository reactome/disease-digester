package org.reactome.server.config;

import org.reactome.server.aspect.ParameterLoggerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Chuan Deng (dengchuanbio@gmail.com)
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
    @Bean
    public ParameterLoggerAspect parameterLoggerAspect() {
        return new ParameterLoggerAspect();
    }
}