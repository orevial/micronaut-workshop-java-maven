package com.stacklabs.micronaut.workshop.agency.domain;

import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import io.micronaut.http.hateoas.Link;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @NotNull
    @Column(name = "registration", nullable = false, unique = true)
    private String registration;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @NotNull
    @Column(name = "category", nullable = false)
    private CarCategory category;

    @Transient
    private List<Link> links;

    public Car(String registration, String brand, String model, CarCategory category) {
        this.registration = registration;
        this.brand = brand;
        this.model = model;
        this.category = category;
    }

    public Car() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(registration, car.registration) &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(model, car.model) &&
                category == car.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(registration, brand, model, category);
    }
}