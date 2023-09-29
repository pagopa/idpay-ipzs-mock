package it.pagopa.mock.idpay.bean;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class AuthTransactionResponse {

    private AuthTransactionResponseOk authTransactionResponseOk;
    private AuthTransactionResponseWrong authTransactionResponseWrong;
}
