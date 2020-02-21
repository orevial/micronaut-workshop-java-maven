package com.stacklabs.micronaut.workshop.agency.persistence;

import com.stacklabs.micronaut.workshop.agency.domain.Car;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class CarRepositoryImpl implements CarRepository {
    @PersistenceContext
    EntityManager entityManager;
    //private EntityManager entityManager;



    public CarRepositoryImpl(@CurrentSession EntityManager entityManager) {
        //this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Car> findAll() {
        return entityManager
                .createQuery("SELECT g FROM Car as g", Car.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Car> findById(@NotNull UUID id) {
        return Optional.ofNullable(entityManager.find(Car.class, id));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Car> findByRegistration(@NotNull String registration) {
        return entityManager.createQuery("FROM Car where registration=:registration", Car.class)
                .setParameter("registration", registration)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    @Override
    public Optional<Car> save(Car car) {
        entityManager.persist(car);
        return findByRegistration(car.getRegistration());
    }

    @Transactional
    @Override
    public void deleteById(@NotNull UUID id) {
        findById(id).ifPresent(car -> entityManager.remove(car));
    }

    @Transactional
    @Override
    public int deleteAll() {
        return entityManager
                .createQuery("DELETE FROM Car")
                .executeUpdate();
    }

    @Transactional
    @Override
    public Optional<Car> update(@NotNull UUID id, @NotBlank Car car) {
        return findById(id)
                .map(any -> entityManager.createQuery(
                        "UPDATE Car g SET brand = :brand, model = :model, category = :category, registration = :registration WHERE id = :id")
                        .setParameter("brand", car.getBrand())
                        .setParameter("model", car.getModel())
                        .setParameter("category", car.getCategory())
                        .setParameter("registration", car.getRegistration())
                        .setParameter("id", id)
                        .executeUpdate())
                .flatMap(any -> findById(id));
    }
}
