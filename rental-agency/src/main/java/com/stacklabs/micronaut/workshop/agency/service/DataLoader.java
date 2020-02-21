package com.stacklabs.micronaut.workshop.agency.service;

import com.stacklabs.micronaut.workshop.agency.config.InitialCar;
import com.stacklabs.micronaut.workshop.agency.domain.Car;
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.discovery.event.ServiceStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Requires(notEnv = {Environment.TEST})
public class DataLoader implements ApplicationEventListener<ServiceStartedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

    private final CarRepository repository;
    private final List<InitialCar> initialCars;

    public DataLoader(CarRepository repository, List<InitialCar> initialCars) {
        this.repository = repository;
        this.initialCars = initialCars;
    }

    @Override
    public void onApplicationEvent(ServiceStartedEvent event) {
        initialCars.stream()
                .map(c -> new Car(c.getRegistration(), c.getBrand(), c.getModel(), c.getCategory()))
                .peek(car -> LOG.info("Going to save car with registration {}...", car.getRegistration()))
                .forEach(car -> repository.save(car)
                        .ifPresentOrElse(
                                any -> LOG.info("Successfuly saved car with registration {}...", car.getRegistration()),
                                () -> LOG.warn("Could not save car with registration {}...", car.getRegistration()))
                );
    }
}