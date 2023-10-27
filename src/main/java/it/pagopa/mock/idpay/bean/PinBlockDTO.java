package it.pagopa.mock.idpay.bean;

import it.pagopa.mock.idpay.ErrorCode;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PinBlockDTO {

    @NotNull(message = "[" + ErrorCode.ERROR_ENCRYPTEDPINBLOCK_MUST_NOT_BE_NULL + "] encryptedPinBlock must not be null")
    private String encryptedPinBlock;

    @NotNull(message = "[" + ErrorCode.ERROR_ENCRYPTEDKEY_MUST_NOT_BE_NULL + "] encryptedKey must not be null")
    private String encryptedKey;

}