package price;

import java.util.HashMap;

import exception.InvalidPriceOperation;

public class PriceFactory {
	private static HashMap<Long,Price> prices = new HashMap<Long,Price>();
	private final static Price market = new Price();

	public static Price makeLimitPrice(String value) throws InvalidPriceOperation {
		value = value.replaceAll("[$,]", "");
		try{
			double tempAmount = Double.valueOf(value).doubleValue();
			long amount = (long) (tempAmount * 100.0);
			if(prices.containsKey(amount))
				return prices.get(amount);
			else{
				prices.put(amount, new LimitPrice(amount));
				return new LimitPrice(amount);
			}
		}
		catch(NumberFormatException e){
			throw new InvalidPriceOperation();
		}
	}
	
	public static Price makeLimitPrice(long value){
		if(prices.containsKey(value))
			return prices.get(value);
		else{
			prices.put(value, new LimitPrice(value));
			return new LimitPrice(value);
		}
	}
	
	public static Price makeMarketPrice(){
		return market;
	}
}
