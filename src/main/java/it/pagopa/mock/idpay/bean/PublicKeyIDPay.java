package it.pagopa.mock.idpay.bean;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@RegisterForReflection
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicKeyIDPay {
    /*
     * Public exponent
     */
    private String e;

    /*
     * Public key use
     */
    private PublicKeyUse use;

    /*
     * Key ID
     */
    private String kid;

    /*
     * Modulus
     */
    private String n;

    /*
     * Key type
     */
    private KeyType kty;

    /*
     * Expiration time
     */
    private long exp;

    /*
     * Issued at
     */
    private long iat;

    private List<KeyOp> keyOps;
}