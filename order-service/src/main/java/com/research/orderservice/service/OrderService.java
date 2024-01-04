package com.research.orderservice.service;

import com.research.orderservice.dto.InventoryResponse;
import com.research.orderservice.dto.OrderLineItemsDto;
import com.research.orderservice.dto.OrderRequest;
import com.research.orderservice.dto.OrderResponse;
import com.research.orderservice.model.Order;
import com.research.orderservice.model.OrderLineItems;
import com.research.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) throws BadRequestException {
        List<OrderLineItems> orderItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::convertToModel).toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderItems)
                .build();

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Check the items is in stock or not

        InventoryResponse[] inventoryResponse = webClientBuilder.build().get()
                .uri("http://inventory-service/api/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponse != null;
        if(inventoryResponse.length == 0){
            throw new BadRequestException("Item's result not found");
        }

        List<String> skuCodeResults = Arrays.stream(inventoryResponse).map(InventoryResponse::getSkuCode)
                .toList();

        boolean allItemInOrderRemainInResult;
        allItemInOrderRemainInResult = new HashSet<>(skuCodeResults).containsAll(skuCodes);
        String message = "Not all items exist. Please try again";

        if(!allItemInOrderRemainInResult){
            log.warn(message);
            throw new IllegalStateException("Not all items exist. Please try again");
        }

        boolean allExist = Arrays.stream(inventoryResponse).allMatch(InventoryResponse::isInStock);
        if(!allExist){
            log.warn(message);
            throw new IllegalStateException("Not all items exist. Please try again");
        }

        orderRepository.save(order);
        log.info("Order placed successfully!");
    }

    public List<OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToDto).toList();
    }

    private OrderLineItems convertToModel(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .quantity(orderLineItemsDto.getQuantity())
                .price(orderLineItemsDto.getPrice())
                .build();
    }

    private OrderLineItemsDto convertToDto(OrderLineItems orderLineItems) {
        return OrderLineItemsDto.builder()
                .id(orderLineItems.getId())
                .skuCode(orderLineItems.getSkuCode())
                .quantity(orderLineItems.getQuantity())
                .price(orderLineItems.getPrice())
                .build();
    }

    private OrderResponse convertToDto(Order order) {
        List<OrderLineItemsDto> itemsDtos = order.getOrderLineItemsList().stream().map(this::convertToDto).toList();
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(itemsDtos)
                .build();
    }
}
