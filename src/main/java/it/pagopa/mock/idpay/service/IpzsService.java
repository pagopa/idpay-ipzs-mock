package it.pagopa.mock.idpay.service;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import it.pagopa.mock.idpay.bean.TransactionStatus;
import it.pagopa.mock.idpay.bean.ipzs.IpzsVerifyCieRequest;
import it.pagopa.mock.idpay.bean.ipzs.IpzsVerifyCieResponse;
import it.pagopa.mock.idpay.bean.ipzs.Outcome;
import it.pagopa.mock.idpay.client.IdpayRestClient;
import it.pagopa.mock.idpay.dao.IdpayTransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class IpzsService {

    @RestClient
    IdpayRestClient idpayRestClient;

    @Inject
    IdpayTransactionRepository idpayTransactionRepository;

    @Inject
    IdpayService idpayService;

    @ConfigProperty(name = "ipzs-call-idpay", defaultValue = "no")
    String ipzsCallIdpay;

    public Uni<IpzsVerifyCieResponse> identitycards(String transactionId, IpzsVerifyCieRequest ipzsVerifyCieRequest) {
        Log.debugf("IpzsService -> identitycards - Input parameters: [%s], [%s]", transactionId, ipzsVerifyCieRequest);

        if ("yes".equalsIgnoreCase(ipzsCallIdpay)) {

            Log.debugf("IpzsService -> identitycards - chiamo IDPAY");

            return idpayRestClient.putAssociateUserTrx(this.getCfByNis(ipzsVerifyCieRequest.getNis()), transactionId)
                    .onItemOrFailure().transformToUni(Unchecked.function((response, error) ->
                            Uni.createFrom().item(new IpzsVerifyCieResponse(Outcome.OK))));
        } else {

            Log.debugf("IpzsService -> identitycards - modifico DB del MOCK");

            return idpayService.getIdpayTransactionEntity(transactionId)
                    .chain(transaction -> {//Transaction found
                        Log.debugf("IdpayService -> cancelTransaction: found idpay transaction [%s]", transactionId);

                        transaction.idpayTransaction.setStatus(TransactionStatus.IDENTIFIED);

                        transaction.idpayTransaction.setRewardCents(Math.round(transaction.idpayTransaction.getAmountCents() / 10d));

                        return idpayTransactionRepository.update(transaction) //updating transaction in DB mil
                                .onFailure().recoverWithItem(err -> {
                                    Log.errorf(err, "IdpayService -> cancelTransaction: Error while updating transaction %s on db", transaction.transactionId);

                                    return transaction;
                                })
                                .chain(() -> Uni.createFrom().item(new IpzsVerifyCieResponse(Outcome.OK)));
                    });
        }
    }

    private String getCfByNis(String nis) {
        String cf = "RSSBNC64T70G677R";

        if ("111122223333".equals(nis))
            cf = "RSSBNC64T70G677R";

        return cf;
    }
}
