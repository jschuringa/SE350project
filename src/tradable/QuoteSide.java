package tradable;

import exception.InvalidTradableOperation;
import price.Price;

class QuoteSide extends Order {

	public QuoteSide(String userName, String productSymbol,
			Price makeLimitPrice, int originalVolume, BookSide side)
			throws InvalidTradableOperation {
		super(userName, productSymbol, makeLimitPrice, originalVolume, side);
		// TODO Auto-generated constructor stub
	}
	
	public QuoteSide(String userName, String productSymbol,
			Price makeLimitPrice, int originalVolume, String side)
			throws InvalidTradableOperation {
		super(userName, productSymbol, makeLimitPrice, originalVolume, side);
		// TODO Auto-generated constructor stub
	}

	/*public QuoteSide (String userName, String productSymbol, Price sidePrice, int originalVolume, BookSide side) throws InvalidTradableOperation{
		
	}
	
	public QuoteSide (QuoteSide qs){
		
	}*/
	
	/*public String getProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	public Price getPrice() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getOriginalVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRemainingVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCancelledVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCancelledVolume(int newCancelledVolume) {
		// TODO Auto-generated method stub

	}

	public void setRemainingVolume(int newRemainingVolume) {
		// TODO Auto-generated method stub

	}

	public String getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSide() {
		// TODO Auto-generated method stub
		return null;
	}*/

	public boolean isQuote() {
		return true;
	}

	/*public String getId() {
		// TODO Auto-generated method stub
		return null;
	}*/
	
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
