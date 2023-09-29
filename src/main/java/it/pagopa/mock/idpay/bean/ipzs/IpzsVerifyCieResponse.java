package it.pagopa.mock.idpay.bean.ipzs;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RegisterForReflection
public class IpzsVerifyCieResponse {
    private Outcome outcome;
}