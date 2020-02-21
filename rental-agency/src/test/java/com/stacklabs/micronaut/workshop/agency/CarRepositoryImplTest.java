package com.stacklabs.micronaut.workshop.agency;

import com.stacklabs.micronaut.workshop.agency.domain.Car;
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository;
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class CarRepositoryImplTest {

    @Inject
    private CarRepository repository;

    private static final Car tesla = new Car("AA-123-BB", "Tesla", "Model S", CarCategory.COUPE);
    private static final Car clio = new Car("ZZ-666-NB", "Renault", "Clio", CarCategory.COMPACT_CAR);

    @Test
    void testCrudOperations() {
        assertThat(repository.findAll()).hasSize(0);

        repository.save(tesla);
        repository.save(clio);
        assertThat(repository.findAll())
                .hasSize(2)
                .containsExactly(tesla, clio);

        assertThat(repository.findById(tesla.getId())).hasValue(tesla);
        assertThat(repository.findByRegistration("ZZ-666-NB")).hasValue(clio);

        repository.deleteById(tesla.getId());
        assertThat(repository.findAll()).hasSize(1);
    }
}