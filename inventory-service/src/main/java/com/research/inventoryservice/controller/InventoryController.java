package com.research.inventoryservice.controller;

import com.research.inventoryservice.dto.InventoryResponse;
import com.research.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    private List<InventoryResponse> getBySkuCodeList(@RequestParam(required = false, name = "skuCode") List<String> skuCodes)
            throws InterruptedException {
//        log.info("Start processing");
//        Thread.sleep(10000);
//        log.info("End processing");
        return inventoryService.findBySkuCodes(skuCodes);
    }

    @GetMapping("all-items")
    private List<InventoryResponse> getBySkuCodeList(){
        return inventoryService.getAll();
    }
}
