package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.CreateOrderDto;
import com.cursor.onlineshop.dtos.OrderDto;
import com.cursor.onlineshop.entities.orders.Order;
import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.UserPermission;
import com.cursor.onlineshop.services.OrderService;
import com.cursor.onlineshop.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    // empty line
    private final OrderService orderService;
    private final UserService userService;

    // add limit, offset and sort params
    @GetMapping
    @Secured("ROLE_ADMIN")
    public List<Order> getAll() {
        return orderService.getAll();
    }


    // DON'T use 302 FOUND, it's redirection code. Use 200 instead
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order requestedOrder = orderService.getById(orderId);

        // move this method to AuthenticationUtils class
        String requestedOrderUserName = userService.getAccountById(requestedOrder.getUser().getAccountId()).getUsername();

        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedOrderUserName) || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return new ResponseEntity<>(requestedOrder, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // Return ResponseEntity object
    @PostMapping
    public Order createOrder(@RequestBody CreateOrderDto newOrderDto) {
        return orderService.add(newOrderDto);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> editOrder(@PathVariable String orderId, @RequestBody OrderDto updatedOrderDto)
            throws ParseException {

        // this line should be in service
        updatedOrderDto.setOrderId(orderId);

        // this line should be in service
        Order requestedOrder = orderService.getById(orderId);

        // move security checks to separate methods/class and throw exception there if something is wrong
        // use @ControllerAdvice to process exceptions
        String requestedOrderUserName = userService
                .getAccountById(requestedOrder.getUser().getAccountId()).getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedOrderUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return new ResponseEntity<>(orderService.update(updatedOrderDto), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
        Order requestedOrder = orderService.getById(orderId);
        String requestedOrderUserName = userService
                .getAccountById(requestedOrder.getUser().getAccountId()).getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedOrderUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return new ResponseEntity<>(orderService.delete(orderId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/exec/{orderId}")
    public ResponseEntity<String> executeOrder(@PathVariable String orderId) {
        Order requestedOrder = orderService.getById(orderId);
        String requestedOrderUserName = userService
                .getAccountById(requestedOrder.getUser().getAccountId()).getUsername();
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedOrderUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return new ResponseEntity<>(orderService.exec(orderId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
