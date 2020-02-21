package com.stacklabs.micronaut.workshop.agency;

import com.stacklabs.micronaut.workshop.agency.domain.Car;
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository;
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RentalsControllerTest {
    private EmbeddedServer server;
    private HttpClient client;
    private CarRepository repository;

    @BeforeEach
    void setup() {
        server = ApplicationContext
                .build()
                .run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
        repository = server.getApplicationContext().getBean(CarRepository.class);
        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        server.close();
    }

    @Test
    void getPrice() {
        // Given
        UUID carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().getId();

        // When
        String response = client.toBlocking()
                .retrieve("/cars/" + carId + "/rentals/_price?nbDays=5&driverAge=35&kilometers=500&insurance=true");

        // Then
        assertThat(response).isEqualTo("This will cost you: 10.0€");
    }

    @Test
    void getPrice_forUnknownCar_return404() {
        // Given
        String carId = "00000000-0000-0000-0000-000000000000";

        // When + Then
        assertThatThrownBy(() ->
                client.toBlocking()
                        .retrieve("/cars/" + carId + "/rentals/_price?nbDays=5&driverAge=35&kilometers=500&insurance=true"))
                .isInstanceOf(Exception.class);
    }

    @Test
    void getPrice_withoutParameters_return404() {
        // Given
        UUID carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().getId();

        // When + Then
        assertThatThrownBy(() -> client.toBlocking().retrieve("/cars/" + carId + "/rentals/_price?nbDays=5&insurance=true"))
                .isInstanceOf(Exception.class);
    }

    private Car createCar(String registration, String brand, String model) {
        return new Car(registration, brand, model, CarCategory.COMPACT_CAR);
    }
}
