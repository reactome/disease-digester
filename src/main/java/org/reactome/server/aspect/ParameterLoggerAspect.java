package org.reactome.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Chuan Deng (dengchuanbio@gmail.com)
 */
@Aspect
@Component
public class ParameterLoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(ParameterLoggerAspect.class);

    @Around("@annotation(org.reactome.server.annotation.ParameterLogger)")
    private Object printParameterLog(ProceedingJoinPoint point) throws Throwable {
        logger.debug(point.getSignature().getDeclaringTypeName() + '.' + point.getSignature().getName() + " with args: ");
        Object[] args = point.getArgs();
        logger.debug(Arrays.toString(args));
        Object proceed = point.proceed(args);
        logger.debug("returned: " + proceed.toString());
        return proceed;
    }
}
