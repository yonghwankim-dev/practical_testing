package yh.kiosk.unit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import yh.kiosk.unit.beverages.Beverage;
import yh.kiosk.unit.order.Order;

@Getter
public class CafeKiosk {

	public static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);
	public static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);

	private final List<Beverage> beverages = new ArrayList<>();

	public void add(Beverage beverage) {
		beverages.add(beverage);
	}

	public void add(Beverage beverage, int count) {
		if (count <= 0) {
			throw new IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.");
		}
		for (int i = 0; i < count; i++) {
			beverages.add(beverage);
		}
	}

	public void remove(Beverage beverage) {
		beverages.remove(beverage);
	}

	public void clear() {
		beverages.clear();
	}

	public int calculateTotalPrice() {
		return beverages.stream().mapToInt(Beverage::getPrice).sum();
	}

	public Order createOrder() {
		LocalTime now = LocalTime.now();
		if (now.isBefore(SHOP_OPEN_TIME) || now.isAfter(SHOP_CLOSE_TIME)) {
			throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요.");
		}
		return new Order(LocalDateTime.now(), beverages);
	}

	public Order createOrder(LocalTime now) {
		if (now.isBefore(SHOP_OPEN_TIME) || now.isAfter(SHOP_CLOSE_TIME)) {
			throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요.");
		}
		return new Order(LocalDateTime.now(), beverages);
	}
}
