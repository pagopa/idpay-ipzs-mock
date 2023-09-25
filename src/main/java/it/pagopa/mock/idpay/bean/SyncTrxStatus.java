package it.pagopa.mock.idpay.bean;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"secondFactor"})
@RegisterForReflection
public class SyncTrxStatus {

    private String id;
    private String idTrxIssuer;
    private String trxCode;
    private Date trxDate;
    private Date authDate;
    private OperationType operationType;
    private Long amountCents;
    private String amountCurrency;
    private String mcc;
    private String acquirerId;
    private String merchantId;
    private String initiativeId;
    private Long rewardCents;
    private List<String> rejectionReasons;
    private TransactionStatus status;
    private byte[] secondFactor;
}