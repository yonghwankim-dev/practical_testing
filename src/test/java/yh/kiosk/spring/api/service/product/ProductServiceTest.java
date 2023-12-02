package yh.kiosk.spring.api.service.product;

import static yh.kiosk.spring.domain.product.ProductSellingStatus.*;
import static yh.kiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import yh.kiosk.spring.IntegrationTestSupport;
import yh.kiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import yh.kiosk.spring.api.service.product.response.ProductResponse;
import yh.kiosk.spring.domain.product.Product;
import yh.kiosk.spring.domain.product.ProductRepository;
import yh.kiosk.spring.domain.product.ProductSellingStatus;
import yh.kiosk.spring.domain.product.ProductType;

class ProductServiceTest extends IntegrationTestSupport {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setup() {
		// 각 테스트 입장에서 보았을때 아예 몰라도 테스트 내용을 이해하는데 묹제 없는가?
		// 수정해도 모든 테스트에 영향을 주지 않는?
		// 위 두조건을 만족하면 beforeEach에 들어가도 좋다!
	}

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가 값이다.")
	@Test
	public void createProduct() {
		// given
		Product product1 = createProduct("001", HANDMADE, SELLING, 4000);
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
		ProductSellingStatus sellingStatus, int price) {
		return Product.builder()
			.productNumber(targetProductNumber)
			.type(type)
			.sellingStatus(sellingStatus)
			.name("아메리카노") // 테스트에 굳이 필요없는 데이터는 파라미터로 받지 않는것도 좋
			.price(price)
			.build();
	}
}
