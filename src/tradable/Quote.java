package tradable;

import exception.InvalidTradableOperation;
import price.Price;

public class Quote {
	private String userName;
	private String product;
	private QuoteSide BUY;
	private QuoteSide SELL;
	
	public Quote(String userName, String productSymbol, Price buyPrice, 
			int buyVolume, Price sellPrice, int sellVolume) throws InvalidTradableOperation{
		this.userName = userName;
		this.product = productSymbol;
		this.BUY = new QuoteSide(this.userName, this.product, buyPrice, buyVolume, BookSide.BUY);
		this.SELL = new QuoteSide(this.userName, this.product, sellPrice, sellVolume, BookSide.BUY);
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public String getProduct(){
		return this.product;
	}
	
	public QuoteSide getQuoteSide(String side) throws InvalidTradableOperation{
		if(side.equalsIgnoreCase("BUY"))
			return this.BUY;
		else if(side.equalsIgnoreCase("SELL"))
			return this.SELL;
		else
			throw new InvalidTradableOperation();
	}
	
	public QuoteSide getQuoteSide(BookSide side) throws InvalidTradableOperation{
		if(side.equals(BookSide.BUY))
			return this.BUY;
		else if(side.equals(BookSide.SELL))
			return this.SELL;
		else
			throw new InvalidTradableOperation();
	}
	
	public String toString(){
		StringBuilder string = new StringBuilder(this.userName);
		string.append(" quote: ");
		string.append(this.product);
		string.append(" ");
		string.append(this.BUY.toString());
		string.append(" - ");
		string.append(this.SELL.toString());
		return string.toString();
	}
}
