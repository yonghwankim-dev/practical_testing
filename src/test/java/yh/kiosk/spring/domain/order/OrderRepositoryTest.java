package yh.kiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static yh.kiosk.spring.domain.product.ProductSellingStatus.*;
import static yh.kiosk.spring.domain.product.ProductType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import yh.kiosk.spring.IntegrationTestSupport;
import yh.kiosk.spring.domain.product.Product;
import yh.kiosk.spring.domain.product.ProductRepository;

@Transactional
class OrderRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@DisplayName("특정 날짜에 발생한 주문들을 조회한다")
	@Test
	public void findOrdersBy() {
		// given
		LocalDateTime registeredDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
		List<Product> products = List.of(
			createProduct("001", 1000),
			createProduct("002", 2000));
		productRepository.saveAll(products);

		Order order1 = Order.create(products, registeredDateTime);
		Order order2 = Order.create(products, registeredDateTime);
		Order order3 = Order.create(products, registeredDateTime);
		order1.changeOrderStatus(OrderStatus.PAYMENT_COMPLETED);
		order2.changeOrderStatus(OrderStatus.PAYMENT_COMPLETED);
		order3.changeOrderStatus(OrderStatus.PAYMENT_COMPLETED);
		orderRepository.saveAll(List.of(order1, order2, order3));

		LocalDateTime startDateTime = LocalDate.of(2023, 1, 1).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.of(2023, 1, 2).atStartOfDay();
		OrderStatus orderStatus = OrderStatus.PAYMENT_COMPLETED;

		// when
		List<Order> orders = orderRepository.findOrdersBy(startDateTime, endDateTime, orderStatus);

		// then
		assertThat(orders).isNotNull();
		assertAll(
			() -> assertThat(orders).hasSize(3),
			() -> assertThat(orders.stream().mapToInt(Order::getTotalPrice).sum()).isEqualTo(9000)
		);
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
