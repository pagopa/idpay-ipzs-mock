package it.pagopa.mock.idpay.service;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.pagopa.mock.idpay.bean.InitiativeResponse;
import it.pagopa.mock.idpay.dao.IdpayTransactionRepository;
import it.pagopa.mock.idpay.dao.Initiative;
import it.pagopa.mock.idpay.dao.InitiativeEntity;
import it.pagopa.mock.idpay.dao.InitiativeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;

import java.util.List;

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

}
