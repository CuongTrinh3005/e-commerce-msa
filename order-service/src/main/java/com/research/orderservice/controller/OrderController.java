package com.research.orderservice.controller;

import com.research.orderservice.dto.OrderRequest;
import com.research.orderservice.dto.OrderResponse;
import com.research.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void placeAnOrder(@RequestBody OrderRequest orderRequest) throws BadRequestException {
        orderService.placeOrder(orderRequest);
    }

    @GetMapping
    public List<OrderResponse> getOrders(){
        return orderService.getOrders();
    }
}
