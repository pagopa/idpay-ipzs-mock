package it.pagopa.mock.idpay.dao;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

@MongoEntity(database = "idpay", collection = "idpayLocalTransactions")
public class IdpayTransactionEntity {

    @BsonId
    public String transactionId;

    public IdpayTransaction idpayTransaction;
}
