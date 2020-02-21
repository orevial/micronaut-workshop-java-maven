package com.stacklabs.micronaut.workshop.registry;

import com.stacklabs.micronaut.workshop.api.v1.AgencyOperations;
import com.stacklabs.micronaut.workshop.api.v1.model.Agency;
import com.stacklabs.micronaut.workshop.registry.service.MongoService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.concurrent.TimeUnit;

@Controller(value = "/agencies")
class AgencyController implements AgencyOperations {

    private MongoService mongoService;

    public AgencyController(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public Maybe<Agency> getById(String id) {
        return mongoService.getById(id);
    }

    public Flowable<Agency> list() {
        return mongoService.list();
    }

    public Single<Agency> save(Agency agency) {
        return mongoService.save(agency);
    }

    @Get("/listWithDelay")
    public Flowable<Agency> listOneSecondEach() {
        return Flowable.zip(
                mongoService.list(),
                Flowable.interval(1, TimeUnit.SECONDS),
                (agency, aLong) -> agency
        );
    }

    // TODO should be at least password protected and reserved to admin accounts
    @Delete("/")
    public Single<String> deleteAll() {
        return mongoService.deleteAll();
    }
}