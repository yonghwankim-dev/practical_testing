package yh.kiosk.spring.api.controller.order;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yh.kiosk.spring.api.controller.order.request.OrderCreateRequest;
import yh.kiosk.spring.api.service.order.OrderService;
import yh.kiosk.spring.api.service.order.response.OrderResponse;

@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/v1/orders/new")
	public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
		return orderService.createOrder(request, LocalDateTime.now());
	}
}
