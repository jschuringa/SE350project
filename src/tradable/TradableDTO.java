package tradable;

import price.Price;

public class TradableDTO {
	public String product;
	public Price price;
	public int originalVolume;
	public int remainingVolume;
	public int cancelledVolume;
	public String user;
	public BookSide side;
	public boolean isQuote;
	public String id;
	
	public String toString(){
		
	}
}
