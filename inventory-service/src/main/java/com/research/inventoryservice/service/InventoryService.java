package com.research.inventoryservice.service;

import com.research.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode){
        return inventoryRepository.existsBySkuCode(skuCode);
    }
}
