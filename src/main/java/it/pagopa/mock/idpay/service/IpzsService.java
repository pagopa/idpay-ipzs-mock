package it.pagopa.mock.idpay.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IpzsService {

/*
    public Uni<InitiativesResponse> getInitiatives(CommonHeader headers) {

        Log.debugf("InitiativesService -> getInitiatives - Input parameters: %s", headers);

        return idpayInitiativesRestClient.getMerchantInitiativeList(headers.getMerchantId())
                .onFailure().transform(t -> {
                    if (t instanceof ClientWebApplicationException webEx && webEx.getResponse().getStatus() == 404) {
                        Log.errorf(t, " InitiativesService -> getInitiatives: idpay NOT FOUND for MerchantId [%s]", headers.getMerchantId());
                        Errors errors = new Errors(List.of(ErrorCode.ERROR_NOT_FOUND_IDPAY_REST_SERVICES), List.of(ErrorCode.ERROR_NOT_FOUND_IDPAY_REST_SERVICES_MSG));
                        return new NotFoundException(Response
                                .status(Response.Status.NOT_FOUND)
                                .entity(errors)
                                .build());
                    } else {
                        Log.errorf(t, "InitiativesService -> getInitiatives: idpay error response for MerchantId [%s]", headers.getMerchantId());
                        Errors errors = new Errors(List.of(ErrorCode.ERROR_CALLING_IDPAY_REST_SERVICES), List.of(ErrorCode.ERROR_CALLING_IDPAY_REST_SERVICES_MSG));
                        return new InternalServerErrorException(Response
                                .status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity(errors)
                                .build());
                    }
                }).map(res -> {
                    Log.debugf("InitiativesService -> getInitiatives: idpay getMerchantInitiativeList service returned a 200 status, response: [%s]", res);

                    LocalDate today = LocalDate.now();

                    List<Initiative> iniList = res.stream().filter(ini ->
                                InitiativeStatus.PUBLISHED == ini.getStatus()  && Boolean.TRUE.equals(ini.getEnabled())
                                        && (today.isAfter(ini.getStartDate()) || today.isEqual(ini.getStartDate()))
                                        && (ini.getEndDate() == null || (today.isBefore(ini.getEndDate()) || today.isEqual(ini.getEndDate())))
                            )
                            .map(fIni -> new Initiative(fIni.getInitiativeId(), fIni.getInitiativeName(), fIni.getOrganizationName())).toList();

                    InitiativesResponse inis = new InitiativesResponse();
                    inis.setInitiatives(iniList);

                    return inis;
                });
    }

 */
}
