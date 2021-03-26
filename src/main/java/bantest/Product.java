package bantest;

public enum Product {
	// add new products here and specify their price
	Banana(110), Apple(120), Juice(130);
	
	private final int priceMiliBan;
	private Product(int priceMiliBan) {
		this.priceMiliBan = priceMiliBan;
	}
	
	public static Product valueOf(int priceMiliBan) {
		for(Product crr : values()) {
			if(crr.priceMiliBan == priceMiliBan)
				return crr;
		}
		return null;
	}

	public int getPriceMiliBan() {
		return priceMiliBan;
	}
	
}
