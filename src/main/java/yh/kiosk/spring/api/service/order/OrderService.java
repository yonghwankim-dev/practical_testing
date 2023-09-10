package yh.kiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import yh.kiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import yh.kiosk.spring.api.service.order.response.OrderResponse;
import yh.kiosk.spring.domain.order.Order;
import yh.kiosk.spring.domain.order.OrderRepository;
import yh.kiosk.spring.domain.product.Product;
import yh.kiosk.spring.domain.product.ProductRepository;
import yh.kiosk.spring.domain.product.ProductType;
import yh.kiosk.spring.domain.stock.Stock;
import yh.kiosk.spring.domain.stock.StockRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

	private final ProductRepository productRepository;

	private final OrderRepository orderRepository;

	private final StockRepository stockRepository;

	/**
	 * 재고 감소 -> 동시성 고민
	 * optimistic lock / pessimistic lock / ...
	 */
	public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {
		List<String> productNumbers = request.getProductNumbers();

		List<Product> products = findProductsBy(productNumbers);

		deductStockQuantities(products);

		// 주문 생성
		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);
		return OrderResponse.of(savedOrder);
	}

	private List<Product> findProductsBy(List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
		Map<String, Product> productMap = products.stream()
			.collect(Collectors.toMap(Product::getProductNumber, product -> product));

		return productNumbers.stream()
			.map(productMap::get)
			.collect(Collectors.toUnmodifiableList());
	}

	private void deductStockQuantities(List<Product> products) {
		List<String> stockProductNumbers = extractStockProductNumbers(products);

		Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
		Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

		// 재고 차감 시도
		for (String productNumber : new HashSet<>(stockProductNumbers)) {
			Stock stock = stockMap.get(productNumber);
			int quantity = productCountingMap.get(productNumber).intValue();
			if (stock.isQuantityLessThan(quantity)) {
				throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
			}
			stock.deductQuantity(quantity);
		}
	}

	private static List<String> extractStockProductNumbers(List<Product> products) {
		return products.stream()
			.filter(product -> ProductType.containsStockType(product.getType()))
			.map(Product::getProductNumber)
			.collect(Collectors.toUnmodifiableList());
	}

	private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
		return stocks.stream()
			.collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
	}

	private Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
		return stockProductNumbers.stream()
			.collect(Collectors.groupingBy(p -> p, Collectors.counting()));
	}
}
