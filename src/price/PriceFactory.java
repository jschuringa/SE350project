package price;

import java.util.regex.Pattern;

public class PriceFactory {
	public static Price makeLimitPrice(String value) throws Exception{
		//Pattern p = Pattern.compile("^[\$|-\$|\$-|-]?[0-9]");
		value.replaceAll("[$,]", "");
		try{
			double amount = Double.valueOf(value).doubleValue();
			amount = amount * 100.0;
			return new Price((long) amount);
		}
		catch(NumberFormatException e){
			throw new Exception();
		}
	}
	
	public static Price makeLimitPrice(long value){
		return new Price(value);
	}
	
	public static Price makeMarketPrice(){
		return new Price();
	}
}
