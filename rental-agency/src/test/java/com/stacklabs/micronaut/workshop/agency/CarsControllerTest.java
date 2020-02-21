package com.stacklabs.micronaut.workshop.agency;

import com.stacklabs.micronaut.workshop.agency.domain.Car;
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository;
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.AbstractMap.SimpleEntry;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CarsControllerTest {
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
    void addCar() throws JSONException {
        // Add a car
        HttpResponse<String> response = addCar(createCar("FIRST-CAR", "Lada", "Model1"));
        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.CREATED.getCode());
        JSONAssert.assertEquals(
                "{" +
                        "        \"registration\":\"FIRST-CAR\"," +
                        "        \"brand\": \"Lada\"," +
                        "        \"model\": \"Model1\"," +
                        "        \"category\": \"COMPACT_CAR\"" +
                        "}",
                response.getBody().get(),
                JSONCompareMode.LENIENT
        );
    }

    @Test
    void addCar_shouldThrowAnException_whenCarAlreadyExist() {
        // Given
        repository.save(createCar("FIRST-CAR", "Lada", "Model1"));

        // When + Then
        assertThatThrownBy(() -> addCar(createCar("FIRST-CAR", "Even other brand should fail", "Another model")))
                .hasMessageContaining("Duplicate record");
    }

    @Test
    void updateCar() {
        // Given
        UUID carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().getId();

        // When
        updateCar(carId.toString(), createCar("FIRST-CAR", "Lada", "Model2"));

        // Then
        assertThat(repository.findById(carId).get().getModel()).isEqualTo("Model2");
    }

    @Test
    void findAllCars_returnEmptyList_onStartup() {
        // Test initial empty list
        SimpleEntry<HttpStatus, String> response = getCars(null, null);
        assertThat(response.getKey().getCode()).isEqualTo(HttpStatus.OK.getCode());
        assertThat(response.getValue()).isEqualTo("[]");
    }

    @Test
    void findAllCars() throws JSONException {
        // Given
        repository.save(createCar("FIRST-CAR", "Lada", "Model1"));
        repository.save(createCar("SECOND-CAR", "Renault", "Capture"));

        // When
        String body = getCars(null, null).getValue();

        // Then
        JSONAssert.assertEquals(
                "[{" +
                        "        \"registration\":\"FIRST-CAR\"," +
                        "        \"brand\": \"Lada\"," +
                        "        \"model\": \"Model1\"," +
                        "        \"category\": \"COMPACT_CAR\"" +
                        "}," +
                        "{" +
                        "        \"registration\":\"SECOND-CAR\"," +
                        "        \"brand\": \"Renault\"," +
                        "        \"model\": \"Capture\"," +
                        "        \"category\": \"COMPACT_CAR\"" +
                        "}]",
                body,
                JSONCompareMode.LENIENT
        );
    }

    @Test
    void findCarById() throws JSONException {
        // Given
        UUID carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().getId();

        // When
        String body = getCars(carId, null).getValue();

        // Then
        JSONAssert.assertEquals(
                "{" +
                        "        \"registration\":\"FIRST-CAR\"," +
                        "        \"brand\": \"Lada\"," +
                        "        \"model\": \"Model1\"," +
                        "        \"category\": \"COMPACT_CAR\"" +
                        "}",
                body,
                JSONCompareMode.LENIENT
        );
    }

    @Test
    void findCarById_withUnknownId_shouldReturn404() {
        assertThatThrownBy(() -> getCars(UUID.fromString("00000000-0000-0000-0000-000000000001"), null))
                .hasMessage("Page Not Found");
    }

    @Test
    void findCarByRegistration() throws JSONException {
        // Given
        repository.save(createCar("FIRST-CAR", "Lada", "Model1"));

        // When
        String body = getCars(null, "FIRST-CAR").getValue();

        // Then
        JSONAssert.assertEquals(
                "[{" +
                        "        \"registration\":\"FIRST-CAR\"," +
                        "        \"brand\": \"Lada\"," +
                        "        \"model\": \"Model1\"," +
                        "        \"category\": \"COMPACT_CAR\"" +
                        "}]",
                body,
                JSONCompareMode.LENIENT
        );
    }

    @Test
    void findCarByRegistration_withUnknownRegistration_shouldReturn404() {
        SimpleEntry<HttpStatus, String> response = getCars(null, "an-unknown-id");
        assertThat(response.getKey().getCode()).isEqualTo(HttpStatus.OK.getCode());
        assertThat(response.getValue()).isEqualTo("[]");
    }

    @Test
    void deleteCar() {
        // Given
        UUID carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().getId();

        // When
        HttpResponse<String> deletedCarResponse = deleteCar(carId.toString());
        assertThat(deletedCarResponse.getStatus().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());
        String body = getCars(null, null).getValue();
        assertThat(body).isEqualTo("[]");
    }

    private HttpResponse<String> addCar(Car car) {
        return client.toBlocking()
                .exchange(HttpRequest.POST("/cars", car), String.class);
    }

    private HttpResponse<String> deleteCar(String carId) {
        return client.toBlocking()
                .exchange(HttpRequest.DELETE("/cars/" + carId), String.class);
    }

    private HttpResponse<String> updateCar(String carId, Car car) {
        return client.toBlocking()
                .exchange(HttpRequest.PUT("/cars/" + carId, car), String.class);
    }

    private SimpleEntry<HttpStatus, String> getCars(UUID id, String registration) {
        String idPath = id != null ? ("/" + id) : "";
        String registrationParam = registration != null ? ("?registration=" + registration) : "";
        HttpResponse<String> response = client.toBlocking().exchange(HttpRequest.GET("/cars" + idPath + registrationParam), String.class);
        return new SimpleEntry<>(response.getStatus(), response.getBody().get());
    }

    private static Car createCar(String registration, String brand, String model) {
        return new Car(registration, brand, model, CarCategory.COMPACT_CAR);
    }
}
