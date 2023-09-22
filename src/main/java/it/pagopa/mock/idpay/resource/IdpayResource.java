package it.pagopa.mock.idpay.resource;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.pagopa.mock.idpay.bean.ErrorResponse;
import it.pagopa.mock.idpay.dao.Initiative;
import it.pagopa.mock.idpay.ErrorCode;
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
    @Path("/idpay/mil/payment/qr-code/merchant/initiatives")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMerchantInitiativeList(@Valid @HeaderParam(value = "x-merchant-id") String merchantId) {

        Log.debugf("IdpayResource -> getMerchantInitiativeList [%s]", merchantId);

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

}
