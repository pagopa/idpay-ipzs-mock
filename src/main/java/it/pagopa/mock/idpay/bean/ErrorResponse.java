package it.pagopa.mock.idpay.bean;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RegisterForReflection
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
}
