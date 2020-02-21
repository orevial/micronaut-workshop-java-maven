package com.stacklabs.micronaut.workshop.agency.config;

import com.stacklabs.micronaut.workshop.agency.PriceCalculatorClient;
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions;
import io.micronaut.health.HealthStatus;
import io.micronaut.management.health.indicator.AbstractHealthIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;

@Singleton
public class CalculatorHealthIndicator extends AbstractHealthIndicator<Map<String, String>> {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorHealthIndicator.class);
    private PriceCalculatorClient calculatorClient;

    public CalculatorHealthIndicator(PriceCalculatorClient calculatorClient) {
        this.calculatorClient = calculatorClient;
    }

    @Override
    protected Map<String, String> getHealthInformation() {
        long before = System.currentTimeMillis();
        sampleRequest();
        long duration = System.currentTimeMillis() - before;
        return Collections.singletonMap("responseTime", duration + " ms");
    }

    @Override
    protected String getName() {
        return "calculator-health";
    }

    private HealthStatus sampleRequest() {
        return calculatorClient
                .calculate(new RentalOptions(CarCategory.COMPACT_CAR, 1, 50, 1000, false))
                .map(any -> HealthStatus.UP)
                .doOnSuccess(any -> LOG.info("Successful request made do rental-calculator server..."))
                .doOnError(any -> healthStatus = HealthStatus.DOWN)
                .doOnError(any -> LOG.error("Unable to reach rental-calculator server !"))
                .blockingGet();
    }
}