package yh.kiosk.spring.api.service.product;

import static yh.kiosk.spring.domain.product.ProductSellingStatus.*;
import static yh.kiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import yh.kiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import yh.kiosk.spring.api.service.product.response.ProductResponse;
import yh.kiosk.spring.domain.product.Product;
import yh.kiosk.spring.domain.product.ProductRepository;
import yh.kiosk.spring.domain.product.ProductSellingStatus;
import yh.kiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가 값이다.")
	@Test
	public void createProduct() {
		// given
		Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		productRepository.save(product1);

		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("카푸치노")
			.price(5000)
			.build();

		// when
		ProductResponse productResponse = productService.createProduct(request.toServiceRequest());
		// then
		Assertions.assertThat(productResponse)
			.extracting("productNumber", "type", "sellingStatus", "name", "price")
			.contains("002", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		Assertions.assertThat(products)
			.hasSize(2)
			.extracting("productNumber", "type", "sellingStatus", "name", "price")
			.containsExactlyInAnyOrder(
				Tuple.tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
				Tuple.tuple("002", HANDMADE, SELLING, "카푸치노", 5000));
	}

	@DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품 번호는 001이다.")
	@Test
	public void createProductWhenProductIsEmpty() {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("카푸치노")
			.price(5000)
			.build();

		// when
		ProductResponse productResponse = productService.createProduct(request.toServiceRequest());
		// then
		Assertions.assertThat(productResponse)
			.extracting("productNumber", "type", "sellingStatus", "name", "price")
			.contains("001", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		Assertions.assertThat(products)
			.hasSize(1)
			.extracting("productNumber", "type", "sellingStatus", "name", "price")
			.containsExactlyInAnyOrder(
				Tuple.tuple("001", HANDMADE, SELLING, "카푸치노", 5000));
	}

	private Product createProduct(String targetProductNumber, ProductType type,
		ProductSellingStatus sellingStatus, String name, int price) {
		return Product.builder()
			.productNumber(targetProductNumber)
			.type(type)
			.sellingStatus(sellingStatus)
			.name(name)
			.price(price)
			.build();
	}
}
