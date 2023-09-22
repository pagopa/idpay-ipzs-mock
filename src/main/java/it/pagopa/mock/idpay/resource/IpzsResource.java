package it.pagopa.mock.idpay.resource;

import it.pagopa.mock.idpay.service.IpzsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path("/initiatives")
public class IpzsResource {

    @Inject
    IpzsService ipzsService;


}
