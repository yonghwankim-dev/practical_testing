package yh.kiosk.spring.api.service.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import yh.kiosk.spring.api.service.mail.MailService;
import yh.kiosk.spring.domain.order.Order;
import yh.kiosk.spring.domain.order.OrderRepository;
import yh.kiosk.spring.domain.order.OrderStatus;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

	private final OrderRepository orderRepository;
	private final MailService mailService;

	public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
		List<Order> orders = orderRepository.findOrdersBy(
			orderDate.atStartOfDay(),
			orderDate.plusDays(1).atStartOfDay(),
			OrderStatus.PAYMENT_COMPLETED
		);

		int totalAmount = orders.stream()
			.mapToInt(Order::getTotalPrice)
			.sum();

		boolean result = mailService.sendMail("no-reploy@cafekiosk.com",
			email,
			String.format("[매출통계] %s", orderDate),
			String.format("총 매출 합계는 %d원 입니다.", totalAmount));
		if (!result) {
			throw new IllegalArgumentException("매출 통계 이메일 전송에 실패했습니다.");
		}
		return true;
	}
}
