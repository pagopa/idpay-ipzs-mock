package it.pagopa.mock.idpay.client.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutAssociateUserTrxDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private Status status;
}
