package yh.kiosk.spring.domain.orderproduct;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yh.kiosk.spring.domain.BaseEntity;
import yh.kiosk.spring.domain.order.Order;
import yh.kiosk.spring.domain.product.Product;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderProduct extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	public OrderProduct(Order order, Product product) {
		this.order = order;
		this.product = product;
	}

}
