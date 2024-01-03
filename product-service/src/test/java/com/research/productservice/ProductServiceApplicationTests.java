package com.research.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.productservice.dto.ProductRequest;
import com.research.productservice.service.ProductService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	static List<ProductRequest> productRequests = new ArrayList<>();

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

	@DynamicPropertySource
	static void setProperties(@NotNull DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeAll
	static void setUpData(){
		ProductRequest request = ProductRequest.builder()
				.name("Macbook Pro M2 Intel Chip")
				.description("Apple Product")
				.price(BigDecimal.valueOf(1400))
				.build();

		ProductRequest request2 = ProductRequest.builder()
				.name("Macbook Pro M1 Apple Chip")
				.description("Apple Product")
				.price(BigDecimal.valueOf(1200))
				.build();

		productRequests.add(request);
		productRequests.add(request2);
	}

	@Test
	@Order(1)
	void contextLoads() {
		Assertions.assertTrue(mongoDBContainer.isCreated());
		Assertions.assertTrue(mongoDBContainer.isRunning());
		System.out.println("Integration Test process is started!");
		System.out.println("Container information: \n");
		System.out.println(mongoDBContainer.getContainerInfo());
	}

	@Test
	@Order(2)
	void shouldCreateProduct() throws Exception {
		ProductRequest requestPayload = getProductPayload();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestPayload)))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, productService.getProducts().size());
	}

	@Test
	void shouldReturnsProductLists() throws Exception {
		productRequests.forEach(productRequest -> productService.createProduct(productRequest));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(3)))
//				.andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[0].['name']").isString())
				.andExpect(jsonPath("$[0].['description']").isString())
				.andExpect(jsonPath("$[0].['price']").isNumber());
	}

	private ProductRequest getProductPayload() {
		return ProductRequest.builder()
				.name("Iphone 15")
				.description("Iphone 15")
				.price(BigDecimal.valueOf(1200))
				.build();
	}
}
