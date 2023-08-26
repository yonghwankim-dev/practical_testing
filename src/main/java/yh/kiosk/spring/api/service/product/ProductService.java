package yh.kiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import yh.kiosk.spring.api.service.product.response.ProductResponse;
import yh.kiosk.spring.domain.product.Product;
import yh.kiosk.spring.domain.product.ProductRepository;
import yh.kiosk.spring.domain.product.ProductSellingStatus;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	public List<ProductResponse> getSellingProducts() {

		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
		return products.stream()
			.map(product -> ProductResponse.of(product))
			.collect(Collectors.toUnmodifiableList());
	}
}
