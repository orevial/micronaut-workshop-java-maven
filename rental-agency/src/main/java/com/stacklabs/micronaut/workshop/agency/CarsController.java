package com.stacklabs.micronaut.workshop.agency;

import com.stacklabs.micronaut.workshop.agency.domain.Car;
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;

import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

@Controller("/cars")
public class CarsController {
    private CarRepository repository;

    public CarsController(CarRepository repository) {
        this.repository = repository;
    }

    @Get("/{?registration}")
    public List<Car> findAll(@QueryValue("registration") Optional<String> registration) {
        return registration
                .map(reg -> repository.findByRegistration(reg)
                        .map(Collections::singletonList)
                        .orElse(emptyList()))
                .orElseGet(() -> repository.findAll());
    }

    @Get("/{id}")
    public Optional<Car> findById(@PathVariable UUID id) {
        return repository.findById(id);
    }


    @Post("/")
    @Status(HttpStatus.CREATED)
    public Car add(@Body Car car) {
        return repository.save(car)
                .orElseThrow(() -> new RuntimeException("Unable to retrieve saved car..."));
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        repository.deleteById(id);
    }

    @Put("/{id}")
    Optional<Car> update(@PathVariable UUID id, @Body Car car) {
        return repository.update(id, car);
    }

    @Error
    public MutableHttpResponse<Object> jsonError(HttpRequest<String> request, PersistenceException constraintViolationException) {
        JsonError error = new JsonError("Duplicate record : " + constraintViolationException.getMessage())
                .link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.status(HttpStatus.CONFLICT, "Given car already exists")
                .body(error);
    }
}