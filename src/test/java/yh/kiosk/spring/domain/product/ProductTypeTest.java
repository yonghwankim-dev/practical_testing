package yh.kiosk.spring.domain.product;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTypeTest {
	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다")
	@Test
	public void containsStockType() {
		// given
		ProductType handmade = ProductType.HANDMADE;
		ProductType bakery = ProductType.BAKERY;
		ProductType bottle = ProductType.BOTTLE;

		// when
		boolean isStockType1 = ProductType.containsStockType(handmade);
		boolean isStockType2 = ProductType.containsStockType(bakery);
		boolean isStockType3 = ProductType.containsStockType(bottle);
		// then
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(isStockType1).isFalse();
			softAssertions.assertThat(isStockType2).isTrue();
			softAssertions.assertThat(isStockType3).isTrue();
			softAssertions.assertAll();
		});
	}
}