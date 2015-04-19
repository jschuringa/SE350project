package tradable;

import price.Price;

public class Quote {
	private String userName;
	private String product;
	private QuoteSide BUY;
	private QuoteSide SELL;
	
	public Quote(String userName, String productSymbol, Price buyPrice, 
			int buyVolume, Price sellPrice, int sellVolume){
		
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public String getProduct(){
		return this.product;
	}
	
	public QuoteSide getQuoteSide(String side){
		
	}
	
	public String toString(){
		
	}
}
