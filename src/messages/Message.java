package messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidMessageException;
import price.Price;
import tradable.BookSide;

public abstract class Message {
	private String user;
	private String product;
	private Price price;
	private int volume;
	private String details;
	private BookSide side;
	private String id;
	
	public Message(String user, String product, Price price, int volume,
			String details, BookSide side, String id) throws InvalidMessageException{
		setUser(user);
		setProduct(product);
		setPrice(price);
		setVolume(volume);
		setDetails(details);
		setBookSide(side);
		setId(id);
	}

	public String getUser() {
		return user;
	}
	
	private void setUser(String user) throws InvalidMessageException{
		if(user == null)
			throw new InvalidMessageException("User cannot be null");
		Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
		Matcher m = p.matcher(user);
		if(m.matches() ){
			this.user = user;
		}
		else{
			throw new InvalidMessageException(user + " is invalid");
		}
	}
	
	public String getProduct() {
		return product;
	}
	
	private void setProduct(String product) throws InvalidMessageException{
		if (product == null)
			throw new InvalidMessageException("Product cannot be null");
		Pattern p = Pattern.compile("[a-zA-Z]{1,4}");
		Matcher m = p.matcher(product);
		if(m.matches() ){
			this.product = product;
		}
		else{
			throw new InvalidMessageException(product + " is invalid product");
		}
	}
	
	public Price getPrice() {
		return price;
	}
	
	private void setPrice(Price price) throws InvalidMessageException{
		if(price != null)
			this.price = price;
		else
			throw new InvalidMessageException(product + " is invalid");
	}

	public int getVolume() {
		return volume;
	}
	
	private void setVolume(int volume) throws InvalidMessageException{
		if(volume >= 0)
			this.volume = volume;
		else
			throw new InvalidMessageException("Volume cannot be negative");
	}

	public String getDetails() {
		return details;
	}
	
	private void setDetails(String details) throws InvalidMessageException {
		if(details == null)
			throw new InvalidMessageException("Details cannot be null");
		if(details.trim().isEmpty())
			throw new InvalidMessageException("Details cannot be empty");
		else
			this.details = details;
	}
	
	public BookSide getBookSide() {
		return side;
	}

	private void setBookSide(BookSide side) throws InvalidMessageException{
		if(side == null)
			throw new InvalidMessageException("Side cannot be null");
		else
			this.side = side;
	}
	
	public String getId() {
		return id;
	}
	
	private void setId(String id) throws InvalidMessageException{
		if(id == null)
			throw new InvalidMessageException("Id cannot be null");
		if(id.trim().isEmpty())
			throw new InvalidMessageException("Id cannot be empty");
		this.id = id;
	}
	
	public String toString(){
		StringBuilder string = new StringBuilder();
		string.append("User: ");
		string.append(this.user);
		string.append(", Product: ");
		string.append(this.product);
		string.append(", Price: ");
		string.append(this.price.toString());
		string.append(", Volume: ");
		string.append(this.volume);
		string.append(", Details: ");
		string.append(this.details);
		string.append(", Side: ");
		string.append(this.side.toString());
		string.append(", Id: ");
		string.append(this.id);
		return string.toString();
	}
	
	public abstract boolean isCancel();
}
