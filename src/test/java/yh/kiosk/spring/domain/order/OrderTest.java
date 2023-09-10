package yh.kiosk.spring.domain.order;

import static yh.kiosk.spring.domain.product.ProductSellingStatus.*;
import static yh.kiosk.spring.domain.product.ProductType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import yh.kiosk.spring.domain.product.Product;

public class OrderTest {
	@DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다")
	@Test
	public void calculateTotalPrice() {
		// given
		List<Product> products = List.of(
			createProduct("001", 1000),
			createProduct("002", 2000));

		// when
		Order order = Order.create(products, LocalDateTime.now());

		// then
		Assertions.assertThat(order.getTotalPrice()).isEqualTo(3000);
	}

	@DisplayName("주문 생성 시 주문 상태는 INIT이다")
	@Test
	public void init() {
		// given
		List<Product> products = List.of(
			createProduct("001", 1000),
			createProduct("002", 2000));

		// when
		Order order = Order.create(products, LocalDateTime.now());

		// then
		Assertions.assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
	}

	@DisplayName("주문 생성 시 주문 등록 시간을 기록한")
	@Test
	public void registeredDateTime() {
		// given
		LocalDateTime registeredDateTime = LocalDateTime.now();
		List<Product> products = List.of(
			createProduct("001", 1000),
			createProduct("002", 2000));

		// when
		Order order = Order.create(products, registeredDateTime);

		// then
		Assertions.assertThat(order.getRegisterDateTime()).isEqualTo(registeredDateTime);
	}

	private Product createProduct(String productNumber, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(HANDMADE)
			.price(price)
			.sellingStatus(SELLING)
			.name("메뉴 이름")
			.build();
	}
}
