package org.poc.shop.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.poc.shop.dto.request.ClientRequest;
import org.poc.shop.dto.response.ClientResponse;
import org.poc.shop.entity.Client;
import org.poc.shop.service.ClientService;
import org.poc.shop.utils.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/clients")
@ApplicationScoped
public class ClientResource {

    @Inject
    private ClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(ClientResource.class);

    @GET
    public Response getAllClients() {
        try {
            List<ClientResponse> clients = clientService.getAllClients();
            return Response.ok(clients).build();
        } catch (Exception e) {
            logger.error("error while getting all clients", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();
        }
    }

    @Transactional
    @POST
    public Response createClient(ClientRequest clientRequest) {
        try {

            Client client = clientService.createClient(clientRequest);
            return Response.ok(client).build();
        } catch (Exception e) {
            logger.error("error while saving client", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();
        }
    }


}
