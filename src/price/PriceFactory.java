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
			//Long amountObj = Long.valueOf(amount);
			if(prices.containsKey(amount))
				return prices.get(amount);
			else{
				prices.put(amount, new Price(amount));
				return new Price(amount);
			}
		}
		catch(NumberFormatException e){
			throw new InvalidPriceOperation();
		}
	}
	
	public static Price makeLimitPrice(long value){
		Long valueObj = Long.valueOf(value);
		if(prices.containsKey(Long.valueOf(valueObj)))
			return prices.get(valueObj);
		else{
			prices.put(valueObj, new Price(value));
			return new Price(value);
		}
	}
	
	public static Price makeMarketPrice(){
		return market;
	}
}
