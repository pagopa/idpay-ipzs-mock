package it.pagopa.mock.idpay.dao;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.codecs.pojo.annotations.BsonId;

@RegisterForReflection
@MongoEntity(database = "idpay", collection = "initiatives")
public class InitiativeEntity {

    @BsonId
    public String initiativeId;
    public Initiative initiative;
}
