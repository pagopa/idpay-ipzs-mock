package it.pagopa.mock.idpay.service;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.pagopa.mock.idpay.bean.TransactionStatus;
import it.pagopa.mock.idpay.bean.ipzs.IpzsVerifyCieRequest;
import it.pagopa.mock.idpay.bean.ipzs.IpzsVerifyCieResponse;
import it.pagopa.mock.idpay.bean.ipzs.Outcome;
import it.pagopa.mock.idpay.dao.IdpayTransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IpzsService {

    @Inject
    IdpayTransactionRepository idpayTransactionRepository;

    @Inject
    IdpayService idpayService;

    public Uni<IpzsVerifyCieResponse> identitycards(String transactionId, IpzsVerifyCieRequest ipzsVerifyCieRequest) {
        Log.debugf("IpzsService -> identitycards - Input parameters: [%s], [%s]", transactionId, ipzsVerifyCieRequest);

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
