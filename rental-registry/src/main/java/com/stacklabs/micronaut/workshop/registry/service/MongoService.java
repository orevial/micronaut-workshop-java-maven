package com.stacklabs.micronaut.workshop.registry.service;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.stacklabs.micronaut.workshop.api.v1.AgencyOperations;
import com.stacklabs.micronaut.workshop.api.v1.model.Agency;
import com.stacklabs.micronaut.workshop.registry.config.AgencyConfiguration;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

@Singleton
public class MongoService implements AgencyOperations {
    private static final Logger LOG = LoggerFactory.getLogger(MongoService.class);

    private final AgencyConfiguration agencyConfiguration;
    private final MongoClient mongo;

    public MongoService(AgencyConfiguration agencyConfiguration, MongoClient mongo) {
        this.agencyConfiguration = agencyConfiguration;
        this.mongo = mongo;
    }

    public Maybe<Agency> getById(String id) {
        return Flowable.fromPublisher(
                getCollection()
                        .find(eq("uuid", id))
                        .limit(1)
        ).firstElement();
    }

    public Flowable<Agency> list() {
        return Flowable.fromPublisher(getCollection().find());
    }

    public Single<Agency> save(Agency agency) {
        LOG.info("Saving {} agency in Mongo...", agency);
        return Single.just(agency)
                .map(a -> new Agency(UUID.randomUUID().toString(), a.getName()))
                .flatMap(a -> Single.fromPublisher(getCollection().insertOne(a))
                        .doOnSuccess(any -> LOG.info("Successfuly saved {} agency to Mongo...", a))
                        .map(any -> a));
    }

    public Single<String> deleteAll() {
        LOG.info("Deleting entire {} collection from Mongo...", agencyConfiguration.getCollectionName());
        return Single.fromPublisher(getCollection().drop())
                .map(success -> "Successfuly deleted entire $ from Mongo...");
    }

    private MongoCollection<Agency> getCollection() {
        return mongo
                .getDatabase(agencyConfiguration.getDatabaseName())
                .getCollection(agencyConfiguration.getCollectionName(), Agency.class);
    }
}