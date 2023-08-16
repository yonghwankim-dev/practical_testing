package yh.kiosk.unit.beverages;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AmericanoTest {
	@Test
	void name() {
		Beverage americano = new Americano();
		Assertions.assertThat(americano.getName()).isEqualTo("아메리카노");
	}

	@Test
	void price() {
		Beverage americano = new Americano();
		Assertions.assertThat(americano.getPrice()).isEqualTo(4000);
	}
}
