package yh.kiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static yh.kiosk.spring.domain.product.ProductSellingStatus.*;
import static yh.kiosk.spring.domain.product.ProductType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import yh.kiosk.spring.IntegrationTestSupport;
import yh.kiosk.spring.domain.history.mail.MailSendHistory;
import yh.kiosk.spring.domain.history.mail.MailSendHistoryRepository;
import yh.kiosk.spring.domain.order.Order;
import yh.kiosk.spring.domain.order.OrderRepository;
import yh.kiosk.spring.domain.order.OrderStatus;
import yh.kiosk.spring.domain.orderproduct.OrderProductRepository;
import yh.kiosk.spring.domain.product.Product;
import yh.kiosk.spring.domain.product.ProductRepository;
import yh.kiosk.spring.domain.product.ProductType;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

	@Autowired
	private OrderStatisticsService orderStatisticsService;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
	}

	@DisplayName("결제 완료 주문들에 대한 매출 통계 메일을 전송한")
	@Test
	public void sendOrderStatisticsMail() {
		// given
		LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 2000);
		Product product3 = createProduct(HANDMADE, "003", 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
		Order order2 = createPaymentCompletedOrder(now, products);
		Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
		Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);
		orderRepository.saveAll(List.of(order1, order2, order3, order4));

		// stubbing
		when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
			.thenReturn(true);
		// when
		boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

		// then
		assertThat(result).isTrue();

		List<MailSendHistory> mailSendHistories = mailSendHistoryRepository.findAll();
		assertThat(mailSendHistories).hasSize(1)
			.extracting("content")
			.contains("총 매출 합계는 12000원 입니다.");
	}

	private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
		Order order = Order.builder()
			.products(products)
			.orderStatus(OrderStatus.PAYMENT_COMPLETED)
			.registerDateTime(now)
			.build();
		orderRepository.save(order);
		return order;
	}

	private Product createProduct(ProductType type, String productNumber, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.price(price)
			.sellingStatus(SELLING)
			.name("메뉴 이름")
			.build();
	}
}
