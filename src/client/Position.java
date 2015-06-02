package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import exception.InvalidPriceOperation;
import price.Price;
import price.PriceFactory;
import tradable.BookSide;


public class Position {
	private HashMap<String, Integer> holdings = new HashMap<String, Integer>();
	private Price accountCosts;
	private HashMap<String, Price> lastSales = new HashMap<String, Price>();
	
	public Position() throws InvalidPriceOperation{
		this.accountCosts = price.PriceFactory.makeLimitPrice("$0.00");
	}
	
	
	public void updatePosition(String product, Price price, BookSide side, int volume) throws InvalidPriceOperation{
		int adjustedVolume;
		if (side == BookSide.BUY){
			adjustedVolume = volume;
		} else {
			adjustedVolume = (0-volume);
		}
		if (!(holdings.containsKey(product))){
			holdings.put(product, adjustedVolume);
		} else {
			int currentHoldingVolume = holdings.get(product);
			adjustedVolume = currentHoldingVolume + adjustedVolume;
			if (adjustedVolume == 0){
				holdings.remove(product);
			} else{
				holdings.put(product, adjustedVolume);
			}
		}
		String price2 = price.toString();
		Price totalPrice =  PriceFactory.makeLimitPrice((Integer.valueOf(price2) * volume));
		if (side == BookSide.BUY){
			accountCosts = accountCosts.subtract(totalPrice);
		} else {
			accountCosts = accountCosts.add(totalPrice);
		}
	}
	
	
	public void updateLastSale(String product, Price price){
		lastSales.put(product, price);
	}
	
	public int getStockPositionVolume(String product){
		if(!(holdings.containsKey(product))){
			return 0;
		} else {
			return holdings.get(product);
		}
	}
	
	public ArrayList<String> getHoldings(){
		ArrayList<String> h = new ArrayList<>(holdings.keySet());
		Collections.sort(h);
		return h;
	}
	
	public Price getStockPositionValue(String product) throws InvalidPriceOperation{
		if (!(holdings.containsKey(product))){
			Price noEntry = price.PriceFactory.makeLimitPrice((long)0.00);
			return noEntry;
		}
		Price Lsale = lastSales.get(product);
		if (Lsale == null){
			Price noLastSale = price.PriceFactory.makeLimitPrice((long)0.00);
			lastSales.put(product, noLastSale);
			return noLastSale;
		}
		int held = holdings.get(product);
		return Lsale.multiply(held);
	}
	
	public Price getAccountCosts(){
		Price accountCosts2 = accountCosts;
		return accountCosts2; 
	}
	
	public Price getAllStockValue() throws InvalidPriceOperation{
		Price allPrice = null;
		for (String p : holdings.keySet()){
			allPrice = allPrice.add(getStockPositionValue(p));
		}
		return allPrice;
	}
	
	public Price getNetAccountValue() throws InvalidPriceOperation{
		return (getAllStockValue().add(getAccountCosts()));
	}
}
