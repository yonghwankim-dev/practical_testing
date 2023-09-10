package yh.kiosk.spring.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
	/**
	 * select *
	 * from product
	 * where selling_type in ('SELLING', 'HOLD')
	 */
	List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

	/**
	 * select *
	 * from product
	 * where id in ('001', '002')
	 */
	List<Product> findAllByProductNumberIn(List<String> productNumbers);

	@Query(value = "select p.product_number from Product p order by p.id desc limit 1", nativeQuery = true)
	String findLatestProductNumber();

}
