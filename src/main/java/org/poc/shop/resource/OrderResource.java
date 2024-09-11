package org.poc.shop.resource;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.poc.shop.dto.request.OrderRequest;
import org.poc.shop.dto.request.OrderUpdateRequest;
import org.poc.shop.dto.response.OrderResponse;
import org.poc.shop.entity.Order;
import org.poc.shop.enums.OrderStatus;
import org.poc.shop.repository.OrderRepository;
import org.poc.shop.service.OrderService;
import org.poc.shop.utils.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrderResource {

    @Inject
    OrderService orderService;

    @Inject
    OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderResource.class);

    @GET
    public Response getAllOrders() {
        try {
            List<OrderResponse> orders = orderService.getAllOrders();
            return Response.ok(orders).build();
        } catch (EntityNotFoundException e) {
            logger.info("error while getting all orders :" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();

        }
    }

    @GET
    @Path("/filter")
    public Response getAllOrdersByStatusAndDate(@QueryParam("status") OrderStatus status, @QueryParam("date") String date) {
        try {
            if (date == null) {
                List<OrderResponse> orders = orderService.getAllOrdersByStatus(status);
                return Response.ok(orders).build();
            } else if (status == null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date deliveryDate = formatter.parse(date);
                List<OrderResponse> orders = orderService.getAllOrdersByDate(deliveryDate);
                return Response.ok(orders).build();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date deliveryDate = formatter.parse(date);
            List<OrderResponse> orders = orderService.getAllOrdersByStatusAndDate(status, deliveryDate);
            return Response.ok(orders).build();
        } catch (Exception e) {
            logger.info("error while filtring orders :" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();

        }
    }


    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") UUID id) {
        try {
            Order order = orderRepository.findById(id);
            return Response.ok(order).build();
        } catch (EntityNotFoundException e) {
            logger.info("error while getting order by id :" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();

        }
    }

    @POST
    @Transactional
    public Response createOrder(OrderRequest order) {
        try {

            OrderResponse orderResponse = orderService.createOrder(order);
            return Response.status(Response.Status.CREATED).entity(orderResponse).build();
        } catch (Exception e) {
            logger.info("Error while creating order" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();
        }
    }


    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteOrder(@PathParam("id") UUID id) {
        try {

            Order order = orderRepository.findById(id);
            if (order == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            order.delete();
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            logger.info("Error while deleting order" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();
        }
    }

    @GET
    @Path("/{id}/pay")
    @Transactional
    public Response payOrder(@PathParam("id") UUID id) {
        try {
            OrderResponse order = orderService.payOrder(id);
            return Response.ok(order).build();
        } catch (Exception e) {
            logger.info("Error while paying " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();
        }
    }


    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateOrderAddress(OrderUpdateRequest orderUpdateRequest, @PathParam("id") UUID orderId) {
        try {
            return Response.ok(orderService.updateOrderAddress(orderUpdateRequest, orderId)).build();
        } catch (Exception e) {
            logger.info("Error while updating order adddress" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(e.getMessage())).build();

        }
    }


}
