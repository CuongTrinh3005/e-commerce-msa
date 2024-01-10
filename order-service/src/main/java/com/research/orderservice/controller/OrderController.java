package com.research.orderservice.controller;

import com.research.orderservice.dto.OrderRequest;
import com.research.orderservice.dto.OrderResponse;
import com.research.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeAnOrder(@RequestBody OrderRequest orderRequest) throws BadRequestException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return orderService.placeOrder(orderRequest);
            }
            catch (Exception e) {
                System.out.println("Enter this block");
                throw new RuntimeException(e);
            }
        });

        completableFuture.completeOnTimeout("TIME_OUT_EXCEPTION", 5000, TimeUnit.SECONDS);

        return completableFuture;
    }

    public CompletableFuture<String> fallbackMethod(@RequestBody OrderRequest orderRequest, RuntimeException runtimeException)
            throws BadRequestException {
        return CompletableFuture.supplyAsync(() -> "Opps, something went wrong! Try to buy after some time!");
    }

    @GetMapping
    public List<OrderResponse> getOrders(){
        return orderService.getOrders();
    }
}
