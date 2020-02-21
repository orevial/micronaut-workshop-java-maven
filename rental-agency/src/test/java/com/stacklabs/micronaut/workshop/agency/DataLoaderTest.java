package com.stacklabs.micronaut.workshop.agency;

import com.stacklabs.micronaut.workshop.agency.config.InitialCar;
import com.stacklabs.micronaut.workshop.agency.domain.Car;
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository;
import com.stacklabs.micronaut.workshop.agency.service.DataLoader;
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@MicronautTest
public class DataLoaderTest {

    @Inject
    private CarRepository carRepository;

    @Inject
    private List<InitialCar> initialCars;

    @BeforeEach
    void setup() {
        carRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        carRepository.deleteAll();
    }

    @Test
    void testDataLoader() {
        // Given
        DataLoader dataLoader = new DataLoader(carRepository, initialCars);

        // When
        dataLoader.onApplicationEvent(null);

        // Then
        assertThat(carRepository.findAll()).hasSize(2);
        // Then
        List<Car> cars = carRepository.findAll();
        assertThat(cars)
                .filteredOn(car -> car.getRegistration().startsWith("test-"))
                .hasSize(4)
                .extracting("brand", "model", "category")
                .contains(
                        tuple("Brand0", "Model0", CarCategory.COUPE),
                        tuple("Brand1", "Model1", CarCategory.CONVERTIBLE),
                        tuple("Brand2", "Model2", CarCategory.MINIVAN),
                        tuple("Brand3", "Model3", CarCategory.STATION_WAGON));
    }
}
