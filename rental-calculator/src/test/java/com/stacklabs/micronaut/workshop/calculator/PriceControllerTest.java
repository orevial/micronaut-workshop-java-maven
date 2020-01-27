package com.stacklabs.micronaut.workshop.calculator;

import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class PriceControllerTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testIndex() {
        RentalOptions rentalOptions = new RentalOptions(CarCategory.COMPACT_CAR, 5, 30, 100, true);
        String price = client.toBlocking().retrieve(HttpRequest.POST("/calculate", rentalOptions));
        assertThat(price).startsWith("114");
    }
}
