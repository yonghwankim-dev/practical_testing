package yh.kiosk.unit;

import java.time.LocalTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import yh.kiosk.unit.beverages.Americano;
import yh.kiosk.unit.beverages.Beverage;
import yh.kiosk.unit.beverages.Latte;
import yh.kiosk.unit.order.Order;

class CafeKioskTest {
	@Test
	@DisplayName("음료 1개를 추가하면 주문 목록에 담긴다")
	void add() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		// when
		cafeKiosk.add(new Americano());
		// then
		Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(1);
		Assertions.assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	@DisplayName("음료를 2개 추가하면 주문 목록에 담긴다")
	void addSeveralBeverage() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		// when
		cafeKiosk.add(americano, 2);
		// then
		Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(2);
		Assertions.assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
		Assertions.assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
	}

	@Test
	@DisplayName("주문 목록에 담긴 아메리카노를 제거할 수 있다")
	void remove() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		cafeKiosk.add(americano);
		Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(1);
		// when
		cafeKiosk.remove(americano);
		// then
		Assertions.assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	@Test
	@DisplayName("주문 목록에 0잔의 음료를 추가할 수 없다")
	public void addZeroBeverages() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		// when
		Assertions.assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
	}

	@Test
	@DisplayName("주문 목록을 비운다")
	void clear() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Beverage americano = new Americano();
		Beverage latte = new Latte();
		cafeKiosk.add(americano);
		cafeKiosk.add(latte);
		Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(2);
		// when
		cafeKiosk.clear();
		// then
		Assertions.assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	@Test
	@DisplayName("주문 목록에 담긴 음료의 주문을 생성한다")
	public void createOrder() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		cafeKiosk.add(americano);
		// when
		Order order = cafeKiosk.createOrder();
		// then
		Assertions.assertThat(order.getBeverages()).hasSize(1);
		Assertions.assertThat(order.getBeverages().get(0)).isEqualTo(americano);
	}

	@Test
	@DisplayName("주문 목록에 담긴 주문 생성시 주문 생성 시간이 담긴다")
	public void createOrderWithCurrentTime() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		cafeKiosk.add(americano);
		// when
		Order order = cafeKiosk.createOrder(LocalTime.of(10, 0));
		// then
		Assertions.assertThat(order.getBeverages()).hasSize(1);
		Assertions.assertThat(order.getBeverages().get(0)).isEqualTo(americano);
	}

	@Test
	@DisplayName("주문 목록에 담긴 주문 생성시 주문 시간 이전이라서 주문을 생성하지 못한다")
	public void createOrderOutsideOpenTime() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		cafeKiosk.add(americano);
		// when
		Assertions.assertThatThrownBy(() -> cafeKiosk.createOrder(LocalTime.of(9, 59)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
	}

	@Test
	@DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다")
	void calculateTotalPrice() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		cafeKiosk.add(americano);
		cafeKiosk.add(latte);

		// when
		int totalPrice = cafeKiosk.calculateTotalPrice();

		// then
		Assertions.assertThat(totalPrice).isEqualTo(8500);
	}
}
