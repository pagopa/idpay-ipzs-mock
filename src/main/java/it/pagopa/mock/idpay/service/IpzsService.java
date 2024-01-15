package it.pagopa.mock.idpay.service;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import it.pagopa.mock.idpay.bean.ipzs.IpzsVerifyCieRequest;
import it.pagopa.mock.idpay.bean.ipzs.IpzsVerifyCieResponse;
import it.pagopa.mock.idpay.bean.ipzs.Outcome;
import it.pagopa.mock.idpay.client.IdpayRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class IpzsService {

    @RestClient
    IdpayRestClient idpayRestClient;

    public Uni<IpzsVerifyCieResponse> identitycards(String transactionId, IpzsVerifyCieRequest ipzsVerifyCieRequest) {
        Log.debugf("IpzsService -> identitycards - Input parameters: [%s], [%s]", transactionId, ipzsVerifyCieRequest);

                return idpayRestClient.putAssociateUserTrx(this.getCfByNis(ipzsVerifyCieRequest.getNis()), transactionId)
                        .onItemOrFailure().transformToUni(Unchecked.function((response, error) ->
                                Uni.createFrom().item(new IpzsVerifyCieResponse(Outcome.OK))));
    }

    private String getCfByNis(String nis) {
        String cf = "RSSBNC64T70G677R";

        if ("111122223333".equals(nis))
            cf = "RSSBNC64T70G677R";

        return cf;
    }
}
