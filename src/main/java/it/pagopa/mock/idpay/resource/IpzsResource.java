package it.pagopa.mock.idpay.resource;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.pagopa.mock.idpay.ErrorCode;
import it.pagopa.mock.idpay.bean.ipzs.IpzsVerifyCieRequest;
import it.pagopa.mock.idpay.service.IpzsService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestQuery;

@Path("")
public class IpzsResource {

    @Inject
    IpzsService ipzsService;

    @POST
    @Path("/api/identitycards")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> identitycards(
            @Valid
            @RestQuery String transactionId,
            @NotNull(message = "[" + ErrorCode.IPZS_REQUEST_MUST_NOT_BE_EMPTY + "] request must not be empty")
            IpzsVerifyCieRequest ipzsVerifyCieRequest) {

        Log.debugf("IpzsResource -> identitycards - Input ipzsVerifyCieRequest: [%s] for transactionId [%s]", ipzsVerifyCieRequest, transactionId);

        return ipzsService.identitycards(transactionId, ipzsVerifyCieRequest).chain(res -> {
            Log.debugf("IpzsResource -> IpzsService -> identitycards - Response [%s]", res);

            return Uni.createFrom().item(
                    Response.status(Response.Status.OK)
                            .entity(res)
                            .build());
        });
    }

}
