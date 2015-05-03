package tradable;

import exception.InvalidTradableOperation;
import price.Price;

public class Quote {
	private String userName;
	private String product;
	private QuoteSide buy;
	private QuoteSide sell;
	
	public Quote(String userName, String productSymbol, Price buyPrice, 
			int buyVolume, Price sellPrice, int sellVolume) throws InvalidTradableOperation{
		this.userName = userName;
		this.product = productSymbol;
		this.buy = new QuoteSide(this.userName, this.product, buyPrice, buyVolume, BookSide.BUY);
		this.sell = new QuoteSide(this.userName, this.product, sellPrice, sellVolume, BookSide.SELL);
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public String getProduct(){
		return this.product;
	}
	
	public QuoteSide getQuoteSide(String side) throws InvalidTradableOperation{
		if(side.equalsIgnoreCase("buy"))
			return this.getQuoteSide(BookSide.BUY);
		else if(side.equalsIgnoreCase("sell"))
			return this.getQuoteSide(BookSide.SELL);
		else
			throw new InvalidTradableOperation("Side not found: " + side);
	}
	
	public QuoteSide getQuoteSide(BookSide side) throws InvalidTradableOperation{
		if(side.equals(BookSide.BUY)){
			QuoteSide temp = new QuoteSide(this.buy.getUser(), this.buy.getProduct(), this.buy.getPrice(), this.buy.getOriginalVolume(), this.buy.getSide());
			temp.setCancelledVolume(this.buy.getCancelledVolume());
			temp.setRemainingVolume(this.buy.getRemainingVolume());
			return temp;
		}
		else if(side.equals(BookSide.SELL)){
			QuoteSide temp = new QuoteSide(this.sell.getUser(), this.sell.getProduct(), this.sell.getPrice(), this.sell.getOriginalVolume(), this.sell.getSide());
			temp.setCancelledVolume(this.sell.getCancelledVolume());
			temp.setRemainingVolume(this.sell.getRemainingVolume());
			return temp;
		}
		else
			throw new InvalidTradableOperation("Side not found: " + side.toString());
	}
	
	public String toString(){
		StringBuilder string = new StringBuilder(this.userName);
		string.append(" quote: ");
		string.append(this.product);
		string.append(" ");
		string.append(this.buy.toString());
		string.append(" - ");
		string.append(this.sell.toString());
		return string.toString();
	}
}
