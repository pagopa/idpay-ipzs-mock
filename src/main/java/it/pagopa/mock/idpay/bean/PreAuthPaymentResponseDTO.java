package it.pagopa.mock.idpay.bean;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RegisterForReflection
public class PreAuthPaymentResponseDTO {
    private String id;
    private String trxCode;
    private Date trxDate;
    private String initiativeId;
    private String initiativeName;
    private String businessName;
    private TransactionStatus status;
    private Long reward;
    private Long amountCents;
    private BigDecimal residualBudget;
    private String secondFactor;
}
