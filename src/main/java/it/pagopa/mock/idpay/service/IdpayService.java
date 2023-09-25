package it.pagopa.mock.idpay.service;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.pagopa.mock.idpay.ErrorCode;
import it.pagopa.mock.idpay.bean.*;
import it.pagopa.mock.idpay.dao.*;
import it.pagopa.swclient.mil.bean.Errors;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class IdpayService {

    @Inject
    IdpayTransactionRepository idpayTransactionRepository;

    @Inject
    InitiativeRepository initiativeRepository;

    //=======================================================
    //Initiatives management services for Mock configuration
    //=======================================================
    public Uni<Void> insertInitiative(Initiative initiative) {

        Log.debugf("IdpayService -> insertInitiative - Input parameters: [%s]", initiative);

        InitiativeEntity entity = new InitiativeEntity();
        entity.initiativeId = initiative.getInitiativeId();
        entity.initiative = initiative;

        return initiativeRepository.persist(entity)
                .onFailure().transform(err-> {
                    Log.errorf(err, "IdpayService -> insertInitiative: Error while storing initiative %s on db", initiative.getInitiativeId());

                    return new InternalServerErrorException(Response
                            .status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(err.getMessage())
                            .build());
                }).chain(() -> Uni.createFrom().voidItem());
    }

    public Uni<List<InitiativeEntity>> getInitiatives() {

        Log.debugf("IdpayService -> getInitiatives");

        return initiativeRepository.listAll();
    }

    public Uni<InitiativeEntity> getInitiative(String initiativeId) {

        Log.debugf("IdpayService -> getInitiative [%s]", initiativeId);

        return initiativeRepository.findById(initiativeId);
    }

    public Uni<Void> deleteInitiative(String initiativeId) {

        Log.debugf("IdpayService -> deleteInitiative - Input parameters: [%s]", initiativeId);

        return initiativeRepository.deleteById(initiativeId)
                .onFailure().transform(err-> {
                    Log.errorf(err, "IdpayService -> deleteInitiative: Error while deleting initiative %s on db", initiativeId);

                    return new InternalServerErrorException(Response
                            .status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(err.getMessage())
                            .build());
                }).chain(() -> Uni.createFrom().voidItem());
    }
    //==========================================================
    //Initiatives management services for Mock configuration END
    //==========================================================

    public Uni<List<InitiativeResponse>> getMerchantInitiativeList(String merchantId) {
        return initiativeRepository.find(
                "initiative.merchantId = ?1", merchantId).list()
                .onFailure().transform(err-> {
                    Log.errorf(err, "IdpayService -> getMerchantInitiativeList: Error while retrieving initiatives for merchantId %s on db", merchantId);

                    return new InternalServerErrorException(Response
                            .status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(err.getMessage())
                            .build());
                }).map(this::getInitiativeResponse);
    }

    private List<InitiativeResponse> getInitiativeResponse(List<InitiativeEntity> entities) {

        List<Initiative> initiatives = entities.stream().map(entity -> entity.initiative).toList();

        return initiatives.stream().map(init ->
                new InitiativeResponse(
                        init.getInitiativeId(),
                        init.getInitiativeName(),
                        init.getOrganizationName(),
                        init.getStatus(),
                        init.getStartDate(),
                        init.getEndDate(),
                        init.getServiceId(),
                        init.getEnabled()
                )).toList();
    }


    public Uni<TransactionResponse> createTransaction(String idpayMerchantId, String xAcquirerId, TransactionCreationRequest transactionCreationRequest) {

        Log.debugf("IdpayService -> createTransaction - Input parameters: [%s], [%s], [%s]", idpayMerchantId, xAcquirerId, transactionCreationRequest);

        return initiativeRepository.find(
                "initiative.merchantId = ?1 and initiative.initiativeId = ?2"
                        , idpayMerchantId,
                        transactionCreationRequest.getInitiativeId()).firstResult()
        .onFailure().transform(err-> {
            Log.errorf(err, "IdpayService -> createTransaction: Error while retrieving initiative for merchantId %s on db", idpayMerchantId);

            return new InternalServerErrorException(Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(err.getMessage())
                    .build());
        })
        .onItem().ifNull().failWith(() -> {
            // if no transaction is found return TRANSACTION_NOT_FOUND
            Log.errorf("IdpayService -> createTransaction: initiative [%s] not found for merchant [%s] on idpay DB", transactionCreationRequest.getInitiativeId(), idpayMerchantId);

            return new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(ErrorCode.INITIATIVE_NOT_FOUND_IDPAY_DB_FOR_MERCHANT, ErrorCode.INITIATIVE_NOT_FOUND_IDPAY_DB_FOR_MERCHANT_MSG))
                    .build());
        })
                .chain(initiative -> {
                    IdpayTransactionEntity entity = createIdpayTransactionEntity(idpayMerchantId, xAcquirerId, transactionCreationRequest);
                    Log.debugf("IdpayService -> createTransaction: storing idpay transaction [%s] on idpay DB", entity);

                    return idpayTransactionRepository.persist(entity)
                            .onFailure().transform(err-> {
                                Log.errorf(err, "IdpayService -> createTransaction: Error while storing transaction %s on db", entity.transactionId);

                                return new InternalServerErrorException(Response
                                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(new ErrorResponse(ErrorCode.ERROR_STORING_DATA_IN_DB, ErrorCode.ERROR_STORING_DATA_IN_DB_MSG))
                                        .build());
                            }).map(this::createTransactionResponseFromEntity);
                });
    }

    private IdpayTransactionEntity createIdpayTransactionEntity(String idpayMerchantId, String xAcquirerId, TransactionCreationRequest transactionCreationRequest) {
        String transactionID = UUID.randomUUID().toString();

        IdpayTransaction idpayTransaction = new IdpayTransaction();

        idpayTransaction.setId(transactionID);
        idpayTransaction.setTrxCode(RandomStringUtils.random(32, true, true));
        idpayTransaction.setInitiativeId(transactionCreationRequest.getInitiativeId());
        idpayTransaction.setMerchantId(idpayMerchantId);
        idpayTransaction.setIdTrxAcquirer(transactionCreationRequest.getIdTrxAcquirer());
        idpayTransaction.setTrxDate(new Date());
        idpayTransaction.setAmountCents(transactionCreationRequest.getAmountCents());
        idpayTransaction.setAmountCurrency("EUR");
        idpayTransaction.setAcquirerId(xAcquirerId);
        idpayTransaction.setStatus(TransactionStatus.CREATED);
        idpayTransaction.setQrcodeTxtUrl(RandomStringUtils.random(32, true, true));

        idpayTransaction.setOperationType(OperationType.CHARGE);
        idpayTransaction.setCounter(1);

        IdpayTransactionEntity entity = new IdpayTransactionEntity();
        entity.transactionId = transactionID;
        entity.idpayTransaction = idpayTransaction;

        return entity;
    }

    private TransactionResponse createTransactionResponseFromEntity(IdpayTransactionEntity entity) {
        TransactionResponse response = new TransactionResponse();

        response.setId(entity.idpayTransaction.getId());
        response.setTrxCode(entity.idpayTransaction.getTrxCode());
        response.setInitiativeId(entity.idpayTransaction.getInitiativeId());
        response.setMerchantId(entity.idpayTransaction.getMerchantId());
        response.setIdTrxAcquirer(entity.idpayTransaction.getIdTrxAcquirer());
        response.setTrxDate(entity.idpayTransaction.getTrxDate());
        response.setAmountCents(entity.idpayTransaction.getAmountCents());
        response.setAmountCurrency(entity.idpayTransaction.getAmountCurrency());
        response.setAcquirerId(entity.idpayTransaction.getAcquirerId());
        response.setStatus(entity.idpayTransaction.getStatus());
        response.setQrcodeTxtUrl(entity.idpayTransaction.getQrcodeTxtUrl());

        return response;
    }

    public Uni<SyncTrxStatus> getTransaction(String idpayMerchantId, String xAcquirerId, String transactionId) {
        Log.debugf("IdpayService -> getTransaction - Input parameters: [%s], [%s], [%s]", idpayMerchantId, xAcquirerId, transactionId);

        return getIdpayTransactionEntity(transactionId) //looking for MilTransactionID in DB
                .chain(transaction -> {//Transaction found
                    Log.debugf("IdpayService -> getTransaction: found idpay transaction [%s]", transactionId);

                    return getInitiativeEntity(transaction.idpayTransaction.getInitiativeId())
                            .chain(initiative -> {
                                if (transaction.idpayTransaction.getCounter() < initiative.initiative.getRetriesStatusChanges()) {
                                    transaction.idpayTransaction.setCounter(transaction.idpayTransaction.getCounter() + 1);
                                    return updateIdpayTransactionEntity(transaction)
                                            .map(this::getTransactionStatusResponseFromEntity);
                                } else {
                                    if (!initiative.initiative.getTransactionFinalStatus().equals(transaction.idpayTransaction.getStatus())) {
                                        IdpayTransactionEntity updTransaction = createFinalIdpayTransactionEntity(transaction, initiative);
                                        return updateIdpayTransactionEntity(updTransaction)
                                                .map(this::getTransactionStatusResponseFromEntity);
                                    }
                                    return Uni.createFrom().item(getTransactionStatusResponseFromEntity(transaction));
                                }
                            });
                });
    }


    private Uni<IdpayTransactionEntity> getIdpayTransactionEntity(String transactionId) {
        return idpayTransactionRepository.findById(transactionId) //looking for idpayTransactionID in DB
                .onFailure().transform(t -> {
                    Log.errorf(t, "[%s] IdpayService -> getIdpayTransactionEntity: Error while retrieving idpay transaction [%s] from DB",
                            ErrorCode.ERROR_RETRIEVING_DATA_FROM_DB, transactionId);
                    return new InternalServerErrorException(Response
                            .status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ErrorResponse(ErrorCode.ERROR_RETRIEVING_DATA_FROM_DB, ErrorCode.ERROR_RETRIEVING_DATA_FROM_DB_MSG))
                            .build());
                })
                .onItem().ifNull().failWith(() -> {
                    // if no transaction is found return TRANSACTION_NOT_FOUND
                    Log.errorf("IdpayService -> getIdpayTransactionEntity: transaction [%s] not found on idpay DB", transactionId);

                    return new NotFoundException(Response
                            .status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse(ErrorCode.ERROR_TRANSACTION_NOT_FOUND_IDPAY_DB, ErrorCode.ERROR_TRANSACTION_NOT_FOUND_IDPAY_DB_MSG))
                            .build());
                });
    }


    private Uni<InitiativeEntity> getInitiativeEntity(String initiativeId) {
        return initiativeRepository.findById(initiativeId) //looking for initiativeID in DB
                .onFailure().transform(t -> {
                    Log.errorf(t, "[%s] IdpayService -> getInitiativeEntity: Error while retrieving initiative [%s] from DB",
                            ErrorCode.ERROR_RETRIEVING_DATA_FROM_DB, initiativeId);
                    return new InternalServerErrorException(Response
                            .status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ErrorResponse(ErrorCode.ERROR_RETRIEVING_DATA_FROM_DB, ErrorCode.ERROR_RETRIEVING_DATA_FROM_DB_MSG))
                            .build());
                })
                .onItem().ifNull().failWith(() -> {
                    // if no transaction is found return TRANSACTION_NOT_FOUND
                    Log.errorf("IdpayService -> getIdpayTransactionEntity: initiative [%s] not found on idpay DB", initiativeId);

                    return new NotFoundException(Response
                            .status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse(ErrorCode.ERROR_INITIATIVE_NOT_FOUND_IDPAY_DB, ErrorCode.ERROR_INITIATIVE_NOT_FOUND_IDPAY_DB_MSG))
                            .build());
                });
    }

    private SyncTrxStatus getTransactionStatusResponseFromEntity(IdpayTransactionEntity entity) {

        SyncTrxStatus response = new SyncTrxStatus();

        response.setId(entity.idpayTransaction.getId());
        response.setTrxCode(entity.idpayTransaction.getTrxCode());
        response.setTrxDate(entity.idpayTransaction.getTrxDate());
        response.setOperationType(entity.idpayTransaction.getOperationType());
        response.setAmountCents(entity.idpayTransaction.getAmountCents());
        response.setAmountCurrency(entity.idpayTransaction.getAmountCurrency());
        response.setAcquirerId(entity.idpayTransaction.getAcquirerId());
        response.setMerchantId(entity.idpayTransaction.getMerchantId());
        response.setInitiativeId(entity.idpayTransaction.getInitiativeId());
        response.setStatus(entity.idpayTransaction.getStatus());

        if (TransactionStatus.IDENTIFIED.equals(entity.idpayTransaction.getStatus())) {
            response.setRewardCents(entity.idpayTransaction.getRewardCents());
            response.setSecondFactor(entity.idpayTransaction.getSecondFactor());
        }

        if (TransactionStatus.AUTHORIZED.equals(entity.idpayTransaction.getStatus())) {
            response.setRewardCents(entity.idpayTransaction.getRewardCents());
        }

        return response;
    }

    private Uni<IdpayTransactionEntity> updateIdpayTransactionEntity(IdpayTransactionEntity entity) {
        return idpayTransactionRepository.update(entity) //updating idpayTransactionID in DB
                .onFailure().transform(t -> {
                    Log.errorf(t, "[%s] IdpayService -> updateIdpayTransactionEntity: Error while updating idpay transaction [%s] from DB",
                            ErrorCode.ERROR_UPDATING_DATA_IN_DB, entity.transactionId);
                    return new InternalServerErrorException(Response
                            .status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ErrorResponse(ErrorCode.ERROR_UPDATING_DATA_IN_DB, ErrorCode.ERROR_UPDATING_DATA_IN_DB_MSG))
                            .build());
                });
    }

    private IdpayTransactionEntity createFinalIdpayTransactionEntity(IdpayTransactionEntity entity, InitiativeEntity initiative) {

        IdpayTransaction idpayTransaction = new IdpayTransaction();

        idpayTransaction.setId(entity.idpayTransaction.getId());
        idpayTransaction.setTrxCode(entity.idpayTransaction.getTrxCode());
        idpayTransaction.setInitiativeId(entity.idpayTransaction.getInitiativeId());
        idpayTransaction.setMerchantId(entity.idpayTransaction.getInitiativeId());
        idpayTransaction.setIdTrxAcquirer(entity.idpayTransaction.getIdTrxAcquirer());
        idpayTransaction.setTrxDate(entity.idpayTransaction.getTrxDate());
        idpayTransaction.setAmountCents(entity.idpayTransaction.getAmountCents());
        idpayTransaction.setAmountCurrency(entity.idpayTransaction.getAmountCurrency());
        idpayTransaction.setAcquirerId(entity.idpayTransaction.getAcquirerId());
        idpayTransaction.setStatus(initiative.initiative.getTransactionFinalStatus());
        idpayTransaction.setQrcodeTxtUrl(entity.idpayTransaction.getQrcodeTxtUrl());
        idpayTransaction.setOperationType(entity.idpayTransaction.getOperationType());

        if (TransactionStatus.IDENTIFIED.equals(idpayTransaction.getStatus())) {
            if (entity.idpayTransaction.getRewardCents() != null) {
                idpayTransaction.setRewardCents(entity.idpayTransaction.getRewardCents());
            } else {
                idpayTransaction.setRewardCents(Math.round(entity.idpayTransaction.getAmountCents() / 10d));
            }

            if (entity.idpayTransaction.getSecondFactor() != null) {
                idpayTransaction.setSecondFactor(entity.idpayTransaction.getSecondFactor());
            } else {
                idpayTransaction.setSecondFactor(RandomStringUtils.random(8, true, true).getBytes(StandardCharsets.UTF_8));
            }
        }

        if (TransactionStatus.AUTHORIZED.equals(idpayTransaction.getStatus())) {
            if (entity.idpayTransaction.getRewardCents() != null) {
                idpayTransaction.setRewardCents(entity.idpayTransaction.getRewardCents());
            } else {
                idpayTransaction.setRewardCents(Math.round(entity.idpayTransaction.getAmountCents() / 10d));
            }
        }

        IdpayTransactionEntity res = new IdpayTransactionEntity();
        res.transactionId = entity.transactionId;
        res.idpayTransaction = idpayTransaction;

        return res;
    }
}
