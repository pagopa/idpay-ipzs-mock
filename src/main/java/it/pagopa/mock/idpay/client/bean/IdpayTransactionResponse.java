package it.pagopa.mock.idpay.client.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdpayTransactionResponse {

    private PutAssociateUserTrxDTO putAssociateUserTrxDTO;
    private TransactionErrorDTO transactionErrorDTO;
}
