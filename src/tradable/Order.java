package tradable;

import price.Price;

class Order implements Tradable {
	
	Order(String userName, String productSymbol, Price orderPrice, int originalVolume, BookSide side){
		 
	}

	public String getProduct() {
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
	}

	public boolean isQuote() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		
	}

}
