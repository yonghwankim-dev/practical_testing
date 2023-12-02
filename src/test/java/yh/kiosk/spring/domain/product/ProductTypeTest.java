package yh.kiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

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

	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다")
	@CsvSource({"HANDMADE,false", "BAKERY,true", "BOTTLE,true"})
	@ParameterizedTest
	public void containsStockType2(ProductType productType, boolean expected) {
		// when
		boolean result = ProductType.containsStockType(productType);

		// then
		assertThat(result).isEqualTo(expected);
	}

	private static Stream<Arguments> provideProductTypesForCheckingStockType() {
		return Stream.of(
			Arguments.of(ProductType.HANDMADE, false),
			Arguments.of(ProductType.BAKERY, true),
			Arguments.of(ProductType.BOTTLE, true)
		);
	}

	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다")
	@MethodSource("provideProductTypesForCheckingStockType")
	@ParameterizedTest
	public void containsStockType3(ProductType productType, boolean expected) {
		// when
		boolean result = ProductType.containsStockType(productType);

		// then
		assertThat(result).isEqualTo(expected);
	}
}
