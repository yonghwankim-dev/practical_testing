package yh.kiosk.spring.api.service.product;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import yh.kiosk.spring.domain.product.ProductRepository;

@Component
@RequiredArgsConstructor
public class ProductNumberFactory {

	private final ProductRepository productRepository;

	public String createNextProductNumber() {
		String latestProductNumber = productRepository.findLatestProductNumber();

		if (latestProductNumber == null) {
			return "001";
		}

		int latestProductNumberInt = Integer.parseInt(latestProductNumber);
		int nextProductNumber = latestProductNumberInt + 1;
		return String.format("%03d", nextProductNumber);
	}
}
