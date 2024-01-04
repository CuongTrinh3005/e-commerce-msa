package com.research.inventoryservice.controller;

import com.research.inventoryservice.dto.InventoryResponse;
import com.research.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    private List<InventoryResponse> getBySkuCodeList(@RequestParam(required = false, name = "skuCode") List<String> skuCodes){
        return inventoryService.findBySkuCodes(skuCodes);
    }

    @GetMapping("all-items")
    private List<InventoryResponse> getBySkuCodeList(){
        return inventoryService.getAll();
    }
}
