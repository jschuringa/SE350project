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
	
	public TradableDTO(String product2, Price price2, int originalVolume2,
			int remainingVolume2, int cancelledVolume2, String user2,
			String side2, boolean quote, String id2) {
		// TODO Auto-generated constructor stub
	}

	public String toString(){
		
	}
}
