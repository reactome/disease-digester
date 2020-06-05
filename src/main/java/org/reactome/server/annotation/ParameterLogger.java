package org.reactome.server.annotation;

import java.lang.annotation.*;

/**
 * @author Chuan Deng (dengchuanbio@gmail.com)
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterLogger {
}