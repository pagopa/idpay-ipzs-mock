package it.pagopa.mock.idpay.resource;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.pagopa.mock.idpay.ErrorCode;
import it.pagopa.mock.idpay.bean.ErrorResponse;
import it.pagopa.mock.idpay.bean.PinBlockDTO;
import it.pagopa.mock.idpay.bean.TransactionCreationRequest;
import it.pagopa.mock.idpay.dao.Initiative;
import it.pagopa.mock.idpay.service.IdpayService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Path("")
public class IdpayResource {
    @Inject
    IdpayService idpayService;

    //=======================================================
    //Initiatives management services for Mock configuration
    //=======================================================
    @POST
    @Path("/mock/initiative")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> insertInitiative(
            @Valid
            @NotNull(message = "[" + ErrorCode.INITIATIVE_MUST_NOT_BE_EMPTY + "] request must not be empty")
            Initiative initiative) {

        Log.debugf("IdpayResource -> createInitiative - Input createInitiative: [%s]", initiative);

        return idpayService.insertInitiative(initiative).chain(() ->
                Uni.createFrom().item(
                        Response.status(Status.NO_CONTENT).build())
        );
    }

    @GET
    @Path("/mock/initiatives")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getInitiatives() {

        Log.debugf("IdpayResource -> getInitiatives");

        return idpayService.getInitiatives().chain(res -> {
            Log.debugf("IdpayResource -> IdpayService -> getInitiatives - Response [%s]", res);

            return Uni.createFrom().item(
                    Response.status(Response.Status.OK)
                            .entity(res)
                            .build());
        });
    }

    @GET
    @Path("/mock/initiatives/{initiativeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getInitiative(@Valid @PathParam(value = "initiativeId") String initiativeId) {

        Log.debugf("IdpayResource -> getInitiative [%s]", initiativeId);

        return idpayService.getInitiative(initiativeId).chain(res -> {
            Log.debugf("IdpayResource -> IdpayService -> getInitiative - Response [%s]", res);

            return Uni.createFrom().item(
                    Response.status(Response.Status.OK)
                            .entity(res)
                            .build());
        });
    }

    @DELETE
    @Path("/mock/initiatives/{initiativeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"PayWithIDPay"})
    public Uni<Response> deleteInitiative(@Valid @PathParam(value = "initiativeId") String initiativeId) {

        Log.debugf("IdpayResource -> deleteInitiative [%s]", initiativeId);

        return idpayService.deleteInitiative(initiativeId).chain(() ->
            Uni.createFrom().item(
                    Response.status(Status.NO_CONTENT).build())
        );
    }
    //==========================================================
    //Initiatives management services for Mock configuration END
    //==========================================================

    @GET
    @Path("/idpay/mil/merchant/initiatives")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMerchantInitiativeList(
            @Valid
            @HeaderParam(value = "x-merchant-fiscalcode") String merchantId,
            @HeaderParam(value = "x-acquirer-id") String acquirerId) {

        Log.debugf("IdpayResource -> getMerchantInitiativeList [%s], [%s]", merchantId, acquirerId);

        return idpayService.getMerchantInitiativeList(merchantId).chain(res -> {
            Log.debugf("IdpayResource -> IdpayService -> getMerchantInitiativeList - Response [%s]", res);

            if (res.isEmpty())
                return Uni.createFrom().item(
                        Response.status(Status.NOT_FOUND)
                                .entity(new ErrorResponse(ErrorCode.INITIATIVES_NOT_FOUND_FOR_MERCHANT, ErrorCode.INITIATIVES_NOT_FOUND_FOR_MERCHANT_MSG))
                                .build());


            return Uni.createFrom().item(
                    Response.status(Response.Status.OK)
                            .entity(res)
                            .build());
        });
    }

    @POST
    @Path("/idpay/mil/payment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createTransaction(
            @Valid
            @HeaderParam(value = "x-merchant-fiscalcode") String idpayMerchantId,
            @HeaderParam(value = "x-acquirer-id") String xAcquirerId,
            @NotNull(message = "[" + ErrorCode.TRANSACTION_CREATION_REQUEST_MUST_NOT_BE_EMPTY + "] request must not be empty")
            TransactionCreationRequest transactionCreationRequest) {

        Log.debugf("IdpayResource -> createTransaction - Input transactionCreationRequest: [%s] for idpayMerchantId [%s]", transactionCreationRequest, idpayMerchantId);

        return idpayService.createTransaction(idpayMerchantId, xAcquirerId, transactionCreationRequest).chain(res -> {
            Log.debugf("IdpayResource -> IdpayService -> createTransaction - Response [%s]", res);

            return Uni.createFrom().item(
                    Response.status(Status.CREATED)
                            .entity(res)
                            .build());
        });
    }

    @GET
    @Path("/idpay/mil/payment/{transactionId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getTransaction(
            @Valid
            @PathParam(value = "transactionId") String transactionId,
            @HeaderParam(value = "x-merchant-fiscalcode") String idpayMerchantId,
            @HeaderParam(value = "x-acquirer-id") String xAcquirerId) {

        Log.debugf("IdpayResource -> getTransaction transactionId: [%s], idpayMerchantId: [%s]", transactionId, idpayMerchantId);

        return idpayService.getTransaction(idpayMerchantId, xAcquirerId, transactionId).chain(res -> {
            Log.debugf("IdpayResource -> IdpayService -> getTransaction - Response [%s]", res);

            return Uni.createFrom().item(
                    Response.status(Response.Status.OK)
                            .entity(res)
                            .build());
        });
    }

    @DELETE
    @Path("/idpay/mil/payment/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> cancelTransaction(
            @Valid
            @PathParam(value = "transactionId") String transactionId,
            @HeaderParam(value = "x-merchant-fiscalcode") String idpayMerchantId,
            @HeaderParam(value = "x-acquirer-id") String xAcquirerId) {

        Log.debugf("IdpayResource -> cancelTransaction transactionId: [%s], idpayMerchantId: [%s]", transactionId, idpayMerchantId);

        return idpayService.cancelTransaction(idpayMerchantId, xAcquirerId, transactionId).chain(() ->
                Uni.createFrom().item(
                        Response.status(Status.OK).build())
        );
    }

    @GET
    @Path("/idpay/mil/payment/publickey")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> retrieveIdpayPublicKey() {

        Log.debugf("IdpayResource -> retrieveIdpayPublicKey");

        return idpayService.retrieveIdpayPublicKey().chain(res -> {
            Log.debugf("IdpayResource -> retrieveIdpayPublicKey -> Response [%s]", res);

            return Uni.createFrom().item(
                    Response.status(Response.Status.OK)
                            .entity(res)
                            .build());
        });
    }

    @PUT
    @Path("/idpay/mil/payment/idpay-code/{idpayTransactionId}/authorize")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> authorize(
            @Valid
            @PathParam(value = "idpayTransactionId") String idpayTransactionId,
            @HeaderParam(value = "x-merchant-fiscalcode") String idpayMerchantId,
            @HeaderParam(value = "x-acquirer-id") String xAcquirerId,
            @NotNull(message = "[" + ErrorCode.TRANSACTION_CREATION_REQUEST_MUST_NOT_BE_EMPTY + "] request must not be empty")
            PinBlockDTO pinBlockDTO) {

        Log.debugf("IdpayResource -> authorize idpayTransactionId: [%s], idpayMerchantId: [%s], xAcquirerId: [%s], authorizeTransaction: [%s]"
                , idpayTransactionId, idpayMerchantId, xAcquirerId, pinBlockDTO);

        return idpayService.authorize(idpayMerchantId, xAcquirerId, idpayTransactionId, pinBlockDTO).chain(res -> {
            Log.debugf("IdpayResource -> authorize -> Response [%s]", res);

            return Uni.createFrom().item(
                    Response.status(Response.Status.OK)
                            .entity(res)
                            .build());
        });
    }

    @PUT
    @Path("/idpay/mil/payment/idpay-code/{transactionId}/preview")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> putPreviewPreAuthPayment(
            @Valid
            @PathParam(value = "transactionId") String transactionId,
            @HeaderParam(value = "x-merchant-fiscalcode") String idpayMerchantId,
            @HeaderParam(value = "x-acquirer-id") String xAcquirerId) {

        Log.debugf("IdpayResource -> putPreviewPreAuthPayment transactionId: [%s], idpayMerchantId: [%s]", transactionId, idpayMerchantId);

        return idpayService.putPreviewPreAuthPayment(idpayMerchantId, xAcquirerId, transactionId).chain(res -> {
            Log.debugf("IdpayResource -> IdpayService -> putPreviewPreAuthPayment - Response [%s]", res);

            return Uni.createFrom().item(
                    Response.status(Response.Status.OK)
                            .entity(res)
                            .build());
        });
    }

}
