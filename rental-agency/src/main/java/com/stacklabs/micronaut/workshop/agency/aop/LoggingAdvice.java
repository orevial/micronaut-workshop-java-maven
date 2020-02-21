package com.stacklabs.micronaut.workshop.agency.aop;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

import static java.util.stream.Collectors.joining;

@Singleton
public class LoggingAdvice<T, V> implements MethodInterceptor<T, V> {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingAdvice.class);

    @Override
    public V intercept(MethodInvocationContext<T, V> context) {
        String attributes = context.getAttributes()
                .asMap().entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(joining(",", "[", "]"));
        String parameters = context.getParameters()
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(joining(",", "[", "]"));
        LOG.info("In {}.{} with attributes {} and params {}...",
                context.getTargetMethod().getDeclaringClass().getSimpleName(),
                context.getMethodName(),
                attributes,
                parameters);

        return context.proceed();
    }
}
