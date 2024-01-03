package com.research.orderservice.service;

import com.research.orderservice.dto.OrderLineItemsDto;
import com.research.orderservice.dto.OrderRequest;
import com.research.orderservice.dto.OrderResponse;
import com.research.orderservice.model.Order;
import com.research.orderservice.model.OrderLineItems;
import com.research.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        List<OrderLineItems> orderItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::convertToModel).toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderItems)
                .build();

        orderRepository.save(order);
    }

    public List<OrderResponse> getOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToDto).toList();
    }

    private OrderLineItems convertToModel(OrderLineItemsDto orderLineItemsDto){
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .quantity(orderLineItemsDto.getQuantity())
                .price(orderLineItemsDto.getPrice())
                .build();
    }

    private OrderLineItemsDto convertToDto(OrderLineItems orderLineItems){
        return OrderLineItemsDto.builder()
                .id(orderLineItems.getId())
                .skuCode(orderLineItems.getSkuCode())
                .quantity(orderLineItems.getQuantity())
                .price(orderLineItems.getPrice())
                .build();
    }

    private OrderResponse convertToDto(Order order){
        List<OrderLineItemsDto> itemsDtos = order.getOrderLineItemsList().stream().map(this::convertToDto).toList();
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(itemsDtos)
                .build();
    }
}
