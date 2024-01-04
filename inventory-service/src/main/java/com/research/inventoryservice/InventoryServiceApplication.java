package com.research.inventoryservice;

import com.research.inventoryservice.model.Inventory;
import com.research.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner loadData (InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = Inventory.builder()
					.skuCode("Iphone 11")
					.quantity(12)
					.build();
			Inventory inventory2 = Inventory.builder()
					.skuCode("Iphone 10")
					.quantity(10)
					.build();

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory2);
		};
	}
}
