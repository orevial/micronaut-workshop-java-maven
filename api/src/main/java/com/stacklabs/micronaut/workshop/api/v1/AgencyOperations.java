package com.stacklabs.micronaut.workshop.api.v1;

import com.stacklabs.micronaut.workshop.api.v1.model.Agency;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface AgencyOperations {

    @Get("/{id}")
    Maybe<Agency> getById(@PathVariable("id") String id);

    @Get("/")
    Flowable<Agency> list();

    @Post("/")
    Single<Agency> save(@Body Agency agency);
}
