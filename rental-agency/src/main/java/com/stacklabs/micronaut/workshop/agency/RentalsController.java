package com.stacklabs.micronaut.workshop.agency;

import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository;
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;

import java.util.Optional;
import java.util.UUID;

@Controller("/cars/{id}/rentals")
public class RentalsController {

    private final CarRepository repository;
    private final PriceCalculatorClient calculator;

    public RentalsController(CarRepository repository, PriceCalculatorClient calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    @Get("/_price{?nbDays,kilometers,driverAge,insurance}")
    public HttpResponse<String> calculateRentalPrice(
            @PathVariable("id") UUID carId,
            @QueryValue("nbDays") Optional<Integer> nbDays,
            @QueryValue("kilometers") Optional<Integer> kilometers,
            @QueryValue("driverAge") Optional<Integer> driverAge,
            @QueryValue("insurance") Optional<Boolean> insurance) {
        return nbDays
                .flatMap(_nbDays -> kilometers
                .flatMap(_kilometers -> driverAge
                .flatMap(_driverAge -> repository.findById(carId)
                .map(_car -> new RentalOptions(_car.getCategory(), _nbDays, _driverAge, _kilometers, insurance.orElse(false))))))
                .flatMap(options -> Optional.ofNullable(calculator.calculate(options).blockingGet()))
                .map(price -> HttpResponse.ok("This will cost you: " + price + "€"))
                .orElseGet(HttpResponse::notFound);
    }
}