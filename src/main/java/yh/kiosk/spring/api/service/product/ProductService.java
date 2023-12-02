package yh.kiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yh.kiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import yh.kiosk.spring.api.service.product.response.ProductResponse;
import yh.kiosk.spring.domain.product.Product;
import yh.kiosk.spring.domain.product.ProductRepository;
import yh.kiosk.spring.domain.product.ProductSellingStatus;

/**
 * readOnly = true : 읽기 전용
 * 읽기 전용으로 되면 어떻게 되는가?
 * CRUD에서 CUD 작업을 안하게 돔/ 오직 조회만 하게 됨
 * JPA : CUD 작업을 하지 않아서 CUD 스냅샷 저장과 변경감지를 안해도 되어 성능이 향상
 * <p>
 * CORS - Command / Read 분리
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductNumberFactory productNumberFactory;

	// 동시성 이슈
	// UUID
	@Transactional
	public ProductResponse createProduct(ProductCreateServiceRequest request) {
		String nextProductNumber = productNumberFactory.createNextProductNumber();
		Product product = request.toEntity(nextProductNumber);
		Product savedProduct = productRepository.save(product);
		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> getSellingProducts() {

		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
		return products.stream()
			.map(product -> ProductResponse.of(product))
			.collect(Collectors.toUnmodifiableList());
	}

}
