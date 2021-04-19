package com.cursor.onlineshop.services;

import com.cursor.onlineshop.dtos.CreateOrderDto;
import com.cursor.onlineshop.dtos.CreateOrderItemDto;
import com.cursor.onlineshop.dtos.OrderDto;
import com.cursor.onlineshop.dtos.OrderItemDto;
import com.cursor.onlineshop.entities.goods.Item;
import com.cursor.onlineshop.entities.orders.Order;
import com.cursor.onlineshop.entities.orders.OrderItem;
import com.cursor.onlineshop.entities.user.User;
import com.cursor.onlineshop.repositories.ItemRepo;
import com.cursor.onlineshop.repositories.OrderRepo;
import com.cursor.onlineshop.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ItemRepo itemRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    public Order add(CreateOrderDto newOrderDto) {
        Order newOrder = new Order();
        String newOrderId = UUID.randomUUID().toString();
        newOrder.setOrderId(newOrderId);
        newOrder.setOrderDate(new Date());
        for (CreateOrderItemDto coidto : newOrderDto.getOrderItemsDtos()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(UUID.randomUUID().toString());
            Item item = itemService.getById(coidto.getItemId());
            orderItem.setItem(item);
            orderItem.setQuantity(coidto.getQuantity());
            orderItem.setPrice(item.getPrice()
                    .multiply(BigDecimal.valueOf(coidto.getQuantity())));
            newOrder.addOrderItem(orderItem);
        }
        newOrder.setOrderPrice(newOrder.getOrderItems().stream()
                .map(OrderItem::getPrice).reduce(BigDecimal::add).orElseThrow());
        orderRepo.save(newOrder);
        User orderer = userService.getUserByAccount(userService.getByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()));
        orderer.addOrder(newOrder);
        orderRepo.save(newOrder);
        userRepo.save(orderer);
        return orderRepo.findById(newOrderId).orElseThrow();
    }

    public Order update(OrderDto updatedOrderDto) throws ParseException {
        Order updatedOrder = getById(updatedOrderDto.getOrderId());
        if (updatedOrderDto.getOrderDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("dd.MM.yyyy");
            updatedOrder.setOrderDate(format.parse(updatedOrderDto.getOrderDate()));
        }
        if (updatedOrderDto.getOrderItemsDtos() != null) {
            updatedOrder.dismissOrderItems();
            for (OrderItemDto oidto : updatedOrderDto.getOrderItemsDtos()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderItemId(UUID.randomUUID().toString());
                Item item = itemService.getById(oidto.getItemId());
                orderItem.setItem(item);
                orderItem.setQuantity(oidto.getQuantity());
                orderItem.setPrice(item.getPrice().multiply(BigDecimal.valueOf(oidto.getQuantity())));
                updatedOrder.addOrderItem(orderItem);
            }
        }
        updatedOrder.setOrderPrice(updatedOrder.getOrderItems().stream()
                .map(OrderItem::getPrice).reduce(BigDecimal::add).orElseThrow());
        if (updatedOrderDto.getAccountId() != null) {
            updatedOrder.getUser().removeOrder(updatedOrder);
            User orderer = userService.getUserByAccount(userService
                    .getAccountById(updatedOrderDto.getAccountId()));
            orderer.addOrder(updatedOrder);
            userRepo.save(orderer);
        }
        orderRepo.save(updatedOrder);
        return orderRepo.findById(updatedOrder.getOrderId()).
                orElseThrow();
    }

    public String delete(String deleteOrderId) {
        orderRepo.deleteById(deleteOrderId);
        return orderRepo.findById(deleteOrderId).isPresent() ?
                String.format("Something went wrong! Order with id=%s is not deleted!", deleteOrderId) :
                String.format("Order with id=%s successfully deleted!", deleteOrderId);
    }

    public String exec(String orderId) {
        Order executingOrder = orderRepo.findById(orderId).orElseThrow();
        for (OrderItem oi : executingOrder.getOrderItems()) {
            Item diminishedItem = itemService.getById(oi.getItem().getItemId());
            int oldAmountInStock = diminishedItem.getAmountInStock();
            if (oldAmountInStock >= oi.getQuantity()) {
                diminishedItem.setAmountInStock(oldAmountInStock - oi.getQuantity());
            } else {
                diminishedItem.setAmountInStock(0);
            }
            itemRepo.save(diminishedItem);
        }
        orderRepo.deleteById(orderId);
        return orderRepo.findById(orderId).isPresent() ?
                String.format("Something went wrong! Order with id=%s is not removed from DB!", orderId) :
                String.format("Order with id=%s successfully removed from DB!", orderId);
    }

    public Order getById(String orderId) {
        return orderRepo.findById(orderId).orElseThrow();
    }

    public List<Order> getAll() {
        return orderRepo.findAll();
    }
}
