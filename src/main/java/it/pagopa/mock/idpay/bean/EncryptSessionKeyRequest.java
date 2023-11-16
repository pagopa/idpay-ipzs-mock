package it.pagopa.mock.idpay.bean;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@RegisterForReflection
@AllArgsConstructor
public class EncryptSessionKeyRequest {

    private String modulus;
    private String exponent;
    private String sessionKey;
}
