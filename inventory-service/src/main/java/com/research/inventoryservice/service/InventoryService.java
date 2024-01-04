package com.research.inventoryservice.service;

import com.research.inventoryservice.dto.InventoryResponse;
import com.research.inventoryservice.model.Inventory;
import com.research.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> findBySkuCodes(List<String> skuCodes){
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<InventoryResponse> getAll(){
        return inventoryRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private InventoryResponse mapToDto(Inventory inventory){
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .quantiy(inventory.getQuantity())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
