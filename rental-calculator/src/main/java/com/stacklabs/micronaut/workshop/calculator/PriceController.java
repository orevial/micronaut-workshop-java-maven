package com.stacklabs.micronaut.workshop.calculator;

import com.stacklabs.micronaut.workshop.api.v1.PriceCalculatorOperations;
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions;
import io.micronaut.http.annotation.Controller;
import io.reactivex.Single;

@Controller("/")
public class PriceController implements PriceCalculatorOperations {

    @Override
    public Single<Float> calculate(RentalOptions options) {
        return Single.just(basePricePerDay(options.getCarCategory()))
                .map(price -> price * driverAgeCoef(options.getDriverAge()))
                .map(price -> price * insuranceCoef(options.hasInsurance()))
                .map(price -> price + kilometersExtra(options.getKilometers()))
                .map(price -> price * options.getNbDays());
    }


    private float driverAgeCoef(int driverAge) {
        return driverAge > 30 ? 1F : 1.2F;
    }

    private float insuranceCoef(boolean hasInsurance) {
        return hasInsurance ? 1.1F : 1F;
    }

    private int kilometersExtra(int kilometers) {
        return kilometers > 1000 ? 50 : 0;
    }

    private int basePricePerDay(CarCategory carCategory) {
        switch (carCategory) {
            case COMPACT_CAR:
                return 19;
            case SUV:
            case STATION_WAGON:
                return 30;
            case MINIVAN:
                return 38;
            case COUPE:
            case CONVERTIBLE:
                return 60;
            default:
                return 1000;
        }
    }
}