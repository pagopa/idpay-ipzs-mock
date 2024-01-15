package it.pagopa.mock.idpay.client;

import io.smallrye.mutiny.Uni;
import it.pagopa.mock.idpay.client.bean.IdpayTransactionResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "idpay-rest-api")
public interface IdpayRestClient {

    @PUT
    @Path("/{transactionId}/user")
    @ClientHeaderParam(name = "Ocp-Apim-Subscription-Key", value = "${idpay-rest-client.apim-subscription-key}", required = false)
    Uni<IdpayTransactionResponse> putAssociateUserTrx(
            @HeaderParam("Fiscal-Code") @NotNull String fiscalCode,
            @PathParam("transactionId") @NotNull String transactionId);
}
