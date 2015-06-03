package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.DataValidationException;
import exception.InvalidBookOperation;
import exception.InvalidPriceOperation;
import price.Price;
import price.PriceFactory;
import tradable.BookSide;

public class Position {

	private HashMap<String, Integer> holdings = new HashMap<String, Integer>();
	private Price accountCosts = PriceFactory.makeLimitPrice(0);
	private HashMap<String, Price> lastSales = new HashMap<String, Price>();
	
	private String validateProduct(String product) throws DataValidationException{
		Pattern p = Pattern.compile("[a-zA-Z]{1,4}");
		Matcher m = p.matcher(product);
		if(product == null || !m.matches() ){
			throw new DataValidationException(product + " is invalid");
		}
		else{
			return product.toUpperCase();
		}
	}
	
	public void updatePosition(String product, Price price, BookSide side, int volume) throws DataValidationException, InvalidPriceOperation{
		product = validateProduct(product);
		if(volume <= 0){
			throw new DataValidationException("Volume must be positive");
		}
		int adjustedVolume;
		if(price == null){
			throw new DataValidationException("Price cannot be null");
		}
		if(side == null){
			throw new DataValidationException("Side cannot be null");
		}
		boolean isBuy = side == BookSide.BUY;
		if(isBuy){
			adjustedVolume = volume;
		}
		else{
			adjustedVolume = -volume;
		}
		if(!holdings.containsKey(product)){
			holdings.put(product, Integer.valueOf(adjustedVolume));
		}
		else{
			int newVolume = holdings.get(product).intValue();
			newVolume += adjustedVolume;
			holdings.put(product, Integer.valueOf(newVolume));
		}
		Price totalPrice = price.multiply(volume);
		if(isBuy){
			accountCosts = accountCosts.subtract(totalPrice);
		}
		else{
			accountCosts = accountCosts.add(totalPrice);
		}
	}
	
	public void updateLastSale(String product, Price price) throws DataValidationException{
		product = validateProduct(product);
		if(price == null){
			throw new DataValidationException("Price cannot be null");
		}
		lastSales.put(product, price);
	}
	
	public int getStockPositionVolume(String product) throws DataValidationException{
		product = validateProduct(product);
		if(!holdings.containsKey(product)){
			return 0;
		}
		return holdings.get(product).intValue();
	}
	
	public ArrayList<String> getHoldings(){
		ArrayList<String> h = new ArrayList<>(holdings.keySet());
		Collections.sort(h);
		return h;
	}
	
	public Price getStockPositionValue(String product) throws DataValidationException, InvalidPriceOperation{
		product = validateProduct(product);
		if(!holdings.containsKey(product)){
			return PriceFactory.makeLimitPrice(0);
		}
		Price p = lastSales.get(product);
		if(p == null){
			p = PriceFactory.makeLimitPrice(0);
		}
		return p.multiply(holdings.get(product).intValue());
	}
	
	public Price getAccountCosts(){
		return accountCosts;
	}
	
	public Price getAllStockValue() throws InvalidPriceOperation, DataValidationException{
		if(holdings.isEmpty()){
			return PriceFactory.makeLimitPrice(0);
		}
		Price p = PriceFactory.makeLimitPrice(0);
		for(String product : holdings.keySet()){
			p = p.add(getStockPositionValue(product));
		}
		return p;
	}
	
	public Price getNetAccountValue() throws InvalidPriceOperation, DataValidationException{
		return getAllStockValue().add(getAccountCosts());
	}
}