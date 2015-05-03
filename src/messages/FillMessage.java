package messages;

import price.Price;
import tradable.BookSide;
import exception.InvalidMessageException;

public class FillMessage extends Message implements Comparable<FillMessage> {

	public FillMessage(String user, String product, Price price, int volume,
			String details, BookSide side, String id)
			throws InvalidMessageException {
		super(user, product, price, volume, details, side, id);
	}

	public boolean isCancel(){
		return false;
	}
	
	@Override
	public int compareTo(FillMessage message) {
		return this.getPrice().compareTo(message.getPrice());
	}

}
