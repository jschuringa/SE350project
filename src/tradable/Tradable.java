package tradable;

import exception.InvalidTradableOperation;
import price.Price;

public interface Tradable {
	String getProduct();
	
	Price getPrice();
	
	int getOriginalVolume();
	
	int getRemainingVolume();
	
	int getCancelledVolume();
	
	void setCancelledVolume(int newCancelledVolume) throws InvalidTradableOperation;
	
	void setRemainingVolume(int newRemainingVolume)throws InvalidTradableOperation;
	
	String getUser();
	
	BookSide getSide();
	
	boolean isQuote();
	
	String getId();
	
}
