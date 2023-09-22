package it.pagopa.mock.idpay.dao;

import io.quarkus.runtime.annotations.RegisterForReflection;
import it.pagopa.mock.idpay.ErrorCode;
import it.pagopa.mock.idpay.bean.InitiativeStatus;
import it.pagopa.mock.idpay.bean.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RegisterForReflection
public class Initiative {

    @NotNull(message = "[" + ErrorCode.ERROR_INITIATIVEID_MUST_NOT_BE_NULL + "] initiativeId must not be null")
    private String initiativeId;

    @NotNull(message = "[" + ErrorCode.ERROR_INITIATIVENAME_MUST_NOT_BE_NULL + "] initiativeName must not be null")
    private String initiativeName;

    @NotNull(message = "[" + ErrorCode.ERROR_ORGANIZATIONNAME_MUST_NOT_BE_NULL + "] organizationName must not be null")
    private String organizationName;

    @NotNull(message = "[" + ErrorCode.ERROR_MERCHANTID_MUST_NOT_BE_NULL + "] merchantId must not be null")
    private String merchantId;

    @NotNull(message = "[" + ErrorCode.ERROR_STATUS_MUST_NOT_BE_NULL + "] status must not be null")
    private InitiativeStatus status;

    @NotNull(message = "[" + ErrorCode.ERROR_STARTDATE_MUST_NOT_BE_NULL + "] startDate must not be null")
    private LocalDate startDate;

    @NotNull(message = "[" + ErrorCode.ERROR_ENDDATE_MUST_NOT_BE_NULL + "] endDate must not be null")
    private LocalDate endDate;

    @NotNull(message = "[" + ErrorCode.ERROR_SERVICEID_MUST_NOT_BE_NULL + "] serviceId must not be null")
    private String serviceId;

    @NotNull(message = "[" + ErrorCode.ERROR_ENABLED_MUST_NOT_BE_NULL + "] enabled must not be null")
    private Boolean enabled;

    @NotNull(message = "[" + ErrorCode.ERROR_TRANSACTIONFINALSTATUS_MUST_NOT_BE_NULL + "] transactionFinalStatus must not be null")
    private TransactionStatus transactionFinalStatus;

    @NotNull(message = "[" + ErrorCode.ERROR_RETRIESSTATUSCHANGES_MUST_NOT_BE_NULL + "] retriesStatusChanges must not be null")
    private int retriesStatusChanges;
}
