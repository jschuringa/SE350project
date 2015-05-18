package tradable;

import exception.InvalidTradableOperation;
import price.Price;

public class QuoteSide extends Order {

	public QuoteSide(String userName, String productSymbol,
			Price makeLimitPrice, int originalVolume, BookSide side)
			throws InvalidTradableOperation {
		super(userName, productSymbol, makeLimitPrice, originalVolume, side);
	}
	
	public QuoteSide(String userName, String productSymbol,
			Price makeLimitPrice, int originalVolume, String side)
			throws InvalidTradableOperation {
		super(userName, productSymbol, makeLimitPrice, originalVolume, side);
	}

	public boolean isQuote() {
		return true;
	}

	public String toString(){
		StringBuilder string = new StringBuilder(this.getPrice().toString());
		string.append(" x ");
		string.append(this.getRemainingVolume());
		string.append(" (Original Vol: ");
		string.append(this.getOriginalVolume());
		string.append(", CXL'd Vol: ");
		string.append(this.getCancelledVolume());
		string.append(") [");
		string.append(this.getId());
		string.append("]");
		return string.toString();
	}
}
