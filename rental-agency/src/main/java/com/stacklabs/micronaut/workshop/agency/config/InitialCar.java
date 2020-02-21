package com.stacklabs.micronaut.workshop.agency.config;

import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;

@EachProperty("app.initial-car")
public class InitialCar {
    private String registration;
    private String brand;
    private String model;
    private CarCategory category;

    public InitialCar(@Parameter String registration) {
        this.registration = registration;
    }

    public String getRegistration() {
        return registration;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }
}