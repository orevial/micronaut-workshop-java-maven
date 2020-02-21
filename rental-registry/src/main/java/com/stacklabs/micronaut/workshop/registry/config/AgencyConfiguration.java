package com.stacklabs.micronaut.workshop.registry.config;

import io.micronaut.context.annotation.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("app.agency")
public interface AgencyConfiguration {
    @NotBlank
    String getDatabaseName();

    @NotBlank
    String getCollectionName();
}