package com.stacklabs.micronaut.workshop.agency;

import com.stacklabs.micronaut.workshop.api.v1.PriceCalculatorOperations;
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.Single;

@Fallback
public class PriceCalculatorClientMock implements PriceCalculatorOperations {
    @Override
    public Single<Float> calculate(RentalOptions options) {
        return Single.just(10F);
    }
}
