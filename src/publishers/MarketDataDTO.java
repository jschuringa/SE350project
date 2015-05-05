package publishers;

import price.Price;

public class MarketDataDTO {
	public String product;
	public Price buyPrice;
	public int buyVolume;
	public Price sellPrice;
	public int sellVolume;
	
	public MarketDataDTO(String product2, Price buyPrice2, int buyVolume2,
			Price sellPrice2, int sellVolume2) {
		this.product = product2;
		this.buyPrice = buyPrice2;
		this.buyVolume = buyVolume2;
		this.sellPrice = sellPrice2;
		this.sellVolume = sellVolume2;
	}
	
	
	
	public String toString(){
		StringBuilder string = new StringBuilder("Product: ");
		string.append(this.product);
		string.append(", Price: ");
		string.append(this.buyPrice.toString());
		string.append(", Buy Price: ");
		string.append(this.buyPrice);
		string.append(", Buy Volume: ");
		string.append(this.buyVolume);
		string.append(", Sell Price: ");
		string.append(this.sellPrice);
		string.append(", Sell Volume: ");
		string.append(this.sellVolume);
		return string.toString();
	}
}
