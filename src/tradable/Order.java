package tradable;

import exception.InvalidTradableOperation;
import price.Price;

public class Order implements Tradable {
	private final String userName;
	private final String product;
	private final String id;
	private final BookSide side;
	private final Price price;
	private int originalVolume;
	private int remainingVolume;
	private int cancelledVolume;

	public Order(String userName, String productSymbol, Price makeLimitPrice,
			int originalVolume, BookSide side) throws InvalidTradableOperation {
		if(originalVolume <= 0)
			throw new InvalidTradableOperation("Volume must be positive value");
		if(side == null)
			throw new InvalidTradableOperation("Invalid side");
		this.userName = userName;
		this.product = productSymbol;
		this.price = makeLimitPrice;
		this.originalVolume = originalVolume;
		this.remainingVolume = this.originalVolume;
		this.cancelledVolume = 0;
		this.side = side;
		this.id = this.userName + this.product + this.price.toString() + System.nanoTime();
	}
	
	public Order(String userName, String productSymbol, Price makeLimitPrice,
			int originalVolume, String side) throws InvalidTradableOperation {
		if(originalVolume <= 0)
			throw new InvalidTradableOperation("Volume must be positive value");
		if(side.equalsIgnoreCase("BUY"))
			this.side = BookSide.BUY;
		else if(side.equalsIgnoreCase("SELL"))
			this.side = BookSide.SELL;
		else
			throw new InvalidTradableOperation();
		this.userName = userName;
		this.product = productSymbol;
		this.price = makeLimitPrice;
		this.originalVolume = originalVolume;
		this.remainingVolume = this.originalVolume;
		this.cancelledVolume = 0;
		this.id = this.userName + this.product + this.price.toString() + System.nanoTime();
	}

	public String getProduct() {
		return this.product;
	}

	public Price getPrice() {
		return this.price;
	}

	public int getOriginalVolume() {
		return this.originalVolume;
	}

	public int getRemainingVolume() {
		return this.remainingVolume;
	}

	public int getCancelledVolume() {
		return this.cancelledVolume;
	}

	public void setCancelledVolume(int newCancelledVolume) throws InvalidTradableOperation {
		if(this.remainingVolume + newCancelledVolume > this.originalVolume 
				|| this.cancelledVolume > newCancelledVolume || newCancelledVolume < 0)
			throw new InvalidTradableOperation("Invalid Cancelled Volume");
		this.cancelledVolume = newCancelledVolume;
	}

	public void setRemainingVolume(int newRemainingVolume) throws InvalidTradableOperation {
		if(this.remainingVolume < newRemainingVolume 
				|| this.cancelledVolume + newRemainingVolume > this.originalVolume || newRemainingVolume < 0)
			throw new InvalidTradableOperation("Invalid Remaining Volume");
		this.remainingVolume = newRemainingVolume;
	}

	public String getUser() {
		return this.userName;
	}

	public BookSide getSide() {
		return this.side;
	}

	public boolean isQuote() {
		return false;
	}

	public String getId() {
		return this.id;
	}
	
	public String toString(){
		StringBuilder string = new StringBuilder(this.userName);
		string.append(" order: ");
		string.append(this.side);
		string.append(" ");
		string.append(this.remainingVolume);
		string.append(" ");
		string.append(this.product);
		string.append(" at ");
		string.append(this.price.toString());
		string.append(" (Original Vol: ");
		string.append(this.originalVolume);
		string.append(", CXL'd Vol: ");
		string.append(this.cancelledVolume);
		string.append("), ID: ");
		string.append(this.id);
		return string.toString();
	}

}
