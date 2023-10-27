package it.pagopa.mock.idpay.dao;

import io.quarkus.runtime.annotations.RegisterForReflection;
import it.pagopa.mock.idpay.bean.OperationType;
import it.pagopa.mock.idpay.bean.TransactionResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RegisterForReflection
public class IdpayTransaction extends TransactionResponse {

    private String idTrxIssuer;
    private Date authDate;
    private OperationType operationType;
    private Long rewardCents;
    private List<String> rejectionReasons;
    private int counter;
}
