package it.pagopa.mock.idpay.dao;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InitiativeRepository implements ReactivePanacheMongoRepositoryBase<InitiativeEntity, String> {
}
