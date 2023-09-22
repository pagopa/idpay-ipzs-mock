package it.pagopa.mock.idpay.bean;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RegisterForReflection
@AllArgsConstructor
public class InitiativeResponse {
    private String initiativeId;
    private String initiativeName;
    private String organizationName;
    private InitiativeStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String serviceId;
    private Boolean enabled;
}