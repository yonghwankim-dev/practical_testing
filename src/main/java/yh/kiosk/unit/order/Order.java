package yh.kiosk.unit.order;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yh.kiosk.unit.beverages.Beverage;

@RequiredArgsConstructor
@Getter
public class Order {

	private final LocalDateTime orderDateTime;
	private final List<Beverage> beverages;
}
