package org.poc.shop.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.poc.shop.dto.request.CartRequest;
import org.poc.shop.entity.Cart;
import org.poc.shop.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/carts")
@ApplicationScoped
public class CartResource {

    @Inject
    private CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(CartResource.class);

    @Transactional
    @GET
    public Response getCarts() {
        try {
            List<Cart> carts = cartService.getAllCarts();
            return Response.ok(carts).build();
        } catch (Exception e) {
            logger.error("error while saving client", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    @Path("{id}")
    @GET
    public Response getCarts(@PathParam("id") UUID id) {
        try {
            Cart cart = cartService.getCart(id);
            return Response.ok(cart).build();
        } catch (Exception e) {
            logger.error("error while saving client", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Transactional
    @POST
    public Response createCart(CartRequest cartRequest) {
        try {
            Cart cart = cartService.createCart(cartRequest);
            return Response.status(Response.Status.CREATED).entity(cart).build();
        } catch (Exception e) {
            logger.error("error while saving cart", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


}
