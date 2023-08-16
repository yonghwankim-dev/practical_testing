package yh.kiosk.unit;

import yh.kiosk.unit.beverages.Americano;
import yh.kiosk.unit.beverages.Latte;

public class CafeKioskRunner {
	public static void main(String[] args) {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());
		cafeKiosk.add(new Latte());

		int total = cafeKiosk.calculateTotalPrice();
		System.out.println("총 주문 가격 : " + total);
	}

}
