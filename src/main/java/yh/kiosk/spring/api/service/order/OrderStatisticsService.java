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
		// 해당 일자에 해당하는 결제 완료된 주문들을 가져와서
		List<Order> orders = orderRepository.findOrdersBy(
			orderDate.atStartOfDay(),
			orderDate.plusDays(1).atStartOfDay(),
			OrderStatus.PAYMENT_COMPLETED
		);

		// 총 매출 합계를 계산하고
		int totalAmount = orders.stream()
			.mapToInt(Order::getTotalPrice)
			.sum();
		// 메일 전송
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