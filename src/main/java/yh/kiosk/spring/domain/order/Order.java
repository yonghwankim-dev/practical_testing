package yh.kiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yh.kiosk.spring.domain.orderproduct.OrderProduct;
import yh.kiosk.spring.domain.product.Product;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
@Entity
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int totalPrice;

	private LocalDateTime registerDateTime;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	@Builder
	private Order(List<Product> products, OrderStatus orderStatus, LocalDateTime registerDateTime) {
		this.orderStatus = orderStatus;
		this.totalPrice = calculateTotalPrice(products);
		this.registerDateTime = registerDateTime;
		this.orderProducts = products.stream()
			.map(product -> new OrderProduct(this, product))
			.collect(Collectors.toUnmodifiableList());
	}

	public static Order create(List<Product> products, LocalDateTime registerDateTime) {
		return Order.builder()
			.products(products)
			.orderStatus(OrderStatus.INIT)
			.registerDateTime(registerDateTime)
			.build();
	}

	private static int calculateTotalPrice(List<Product> products) {
		return products.stream()
			.mapToInt(Product::getPrice)
			.sum();
	}

	public void changeOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
}
