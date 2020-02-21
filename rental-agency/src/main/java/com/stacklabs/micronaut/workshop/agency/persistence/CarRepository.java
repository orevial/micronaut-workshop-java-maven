package com.stacklabs.micronaut.workshop.agency.persistence;

import com.stacklabs.micronaut.workshop.agency.domain.Car;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
    List<Car> findAll();
    Optional<Car> findById(@NotNull UUID id);
    Optional<Car> findByRegistration(@NotNull String registration);
    Optional<Car> save(Car car);
    void deleteById(@NotNull UUID id);
    int deleteAll();
    Optional<Car> update(@NotNull UUID id, @NotBlank Car car);
}
