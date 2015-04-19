package tradable;

import price.Price;

public class TradableDTO {
	public String product;
	public Price price;
	public int originalVolume;
	public int remainingVolume;
	public int cancelledVolume;
	public String user;
	public String side;
	public boolean isQuote;
	public String id;
	
	public TradableDTO(String product2, Price price2, int originalVolume2,
			int remainingVolume2, int cancelledVolume2, String user2,
			String side2, boolean quote, String id2) {
		this.product = product2;
		this.price = price2;
		this.originalVolume = originalVolume2;
		this.remainingVolume = remainingVolume2;
		this.cancelledVolume = cancelledVolume2;
		this.user = user2;
		this.side = side2;
		this.isQuote = quote;
		this.id = id2;
	}

	public String toString(){
		StringBuilder string = new StringBuilder("Product: ");
		string.append(this.product);
		string.append(", Price: ");
		string.append(this.price.toString());
		string.append(", OriginalVolume: ");
		string.append(this.originalVolume);
		string.append(", RemainingVolume: ");
		string.append(this.remainingVolume);
		string.append(", CancelledVolume: ");
		string.append(this.cancelledVolume);
		string.append(", User: ");
		string.append(this.user);
		string.append(", Side: ");
		string.append(this.side);
		string.append(", isQuote ");
		string.append(this.isQuote);
		string.append(", ID: ");
		string.append(this.id);
		return string.toString();
	}
}
