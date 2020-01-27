package com.stacklabs.micronaut.workshop.api.v1.model;

public class Agency {
    private String uuid = "some-uuid";
    private String name = "Some name";

    public Agency() {
    }

    public Agency(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}