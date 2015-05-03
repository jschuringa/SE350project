package tradable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidTradableOperation;
import price.Price;

public class Order implements Tradable {
	private final String userName;
	private final String product;
	private final String id;
	private final BookSide side;
	private final Price price;
	private final int originalVolume;
	private int remainingVolume;
	private int cancelledVolume;

	public Order(String userName, String productSymbol, Price makeLimitPrice,
			int originalVolume, BookSide side) throws InvalidTradableOperation {
		if(originalVolume <= 0)
			throw new InvalidTradableOperation("Volume must be positive value");
		this.userName = this.setUser(userName);
		this.product = this.setProduct(productSymbol);
		this.price = this.setPrice(makeLimitPrice);
		this.originalVolume = originalVolume;
		this.remainingVolume = this.originalVolume;
		this.cancelledVolume = 0;
		this.side = this.setSide(side);
		this.id = this.userName + this.product + this.price.toString() + System.nanoTime();
	}
	
	public Order(String userName, String productSymbol, Price makeLimitPrice,
			int originalVolume, String side) throws InvalidTradableOperation {
		if(originalVolume <= 0)
			throw new InvalidTradableOperation("Volume must be positive value");
		this.userName = this.setUser(userName);
		this.product = this.setProduct(productSymbol);
		this.price = this.setPrice(makeLimitPrice);
		this.originalVolume = originalVolume;
		this.remainingVolume = this.originalVolume;
		this.cancelledVolume = 0;
		this.side = this.setSide(side);
		this.id = this.userName + this.product + this.price.toString() + System.nanoTime();
	}

	public String getProduct() {
		return this.product;
	}
	
	private String setProduct(String productName) throws InvalidTradableOperation {
		Pattern p = Pattern.compile("[a-zA-Z]{1,4}");
		Matcher m = p.matcher(productName);
		if(productName != null && m.matches() ){
			return productName;
		}
		else{
			throw new InvalidTradableOperation(productName + " is invalid");
		}
	}

	public Price getPrice() {
		return this.price;
	}
	
	private Price setPrice(Price p) throws InvalidTradableOperation{
		if(p == null)
			throw new InvalidTradableOperation("Price cannot be null");
		if(!p.isNegative()){
			return p;
		}
		else{
			throw new InvalidTradableOperation("Price is invalid");
		}
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
	
	private String setUser(String userName) throws InvalidTradableOperation{
		Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
		Matcher m = p.matcher(userName);
		if(userName != null && m.matches() ){
			return userName;
		}
		else{
			throw new InvalidTradableOperation(userName + " is invalid");
		}
	}

	public BookSide getSide() {
		return this.side;
	}
	
	private BookSide setSide(BookSide side) throws InvalidTradableOperation{
		if(side != null)
			return side;
		else
			throw new InvalidTradableOperation("Side cannot be null");
	}
	
	private BookSide setSide(String side) throws InvalidTradableOperation{
		if(side == null)
			throw new InvalidTradableOperation("Side cannot be null");
		if(side.equalsIgnoreCase("BUY"))
			return setSide(BookSide.BUY);
		else if(side.equalsIgnoreCase("SELL"))
			return setSide(BookSide.SELL);
		else
			throw new InvalidTradableOperation("Invalid value for side");
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
