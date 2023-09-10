package yh.kiosk.spring.api.service.product.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yh.kiosk.spring.domain.product.Product;
import yh.kiosk.spring.domain.product.ProductSellingStatus;
import yh.kiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCreateServiceRequest {

	private ProductType type;

	private ProductSellingStatus sellingStatus;

	private String name;

	private int price;

	@Builder
	private ProductCreateServiceRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
		this.type = type;
		this.sellingStatus = sellingStatus;
		this.name = name;
		this.price = price;
	}

	public Product toEntity(String nextProductNumber) {
		return Product.builder()
			.productNumber(nextProductNumber)
			.type(type)
			.sellingStatus(sellingStatus)
			.name(name)
			.price(price)
			.build();
	}
}
