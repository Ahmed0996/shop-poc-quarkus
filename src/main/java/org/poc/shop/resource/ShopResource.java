package org.poc.shop.resource;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.poc.shop.dto.response.OrderResponse;
import org.poc.shop.entity.Shop;
import org.poc.shop.service.OrderService;
import org.poc.shop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/shops")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ShopResource {
    private static final Logger logger = LoggerFactory.getLogger(ShopResource.class);

    @Inject
    private ShopService shopService;

    @Inject
    private OrderService orderService;

    @GET
    public Response getAllShops() {
        try {
            List<Shop> shops = shopService.getAllShops();
            return Response.ok(shops).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}/delivery")
    public Response deliverOrders(@QueryParam("date") String date, @PathParam("id") Long shopId) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date deliveryDate = formatter.parse(date);
            List<OrderResponse> deliveryOrders = orderService.ordersDeliveryArrangement(deliveryDate, shopId);
            return Response.ok(deliveryOrders).build();
        } catch (Exception e) {
            logger.info("Error while arranging delivery {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
