package yh.kiosk.unit;

import java.time.LocalTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import yh.kiosk.unit.beverages.Americano;
import yh.kiosk.unit.beverages.Beverage;
import yh.kiosk.unit.beverages.Latte;
import yh.kiosk.unit.order.Order;

class CafeKioskTest {
	@Test
	void add() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());
		Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(1);
		Assertions.assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void addSeveralBeverage() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		cafeKiosk.add(americano, 2);
		Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(2);
		Assertions.assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
		Assertions.assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
	}

	@Test
	void remove() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		cafeKiosk.add(americano);
		Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(1);

		cafeKiosk.remove(americano);
		Assertions.assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	@Test
	public void addZeroBeverages() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		Assertions.assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
	}

	@Test
	void clear() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Beverage americano = new Americano();
		Beverage latte = new Latte();
		cafeKiosk.add(americano);
		cafeKiosk.add(latte);
		Assertions.assertThat(cafeKiosk.getBeverages()).hasSize(2);

		cafeKiosk.clear();
		Assertions.assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	@Test
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
}
