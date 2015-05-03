package messages;

import exception.InvalidMessageException;
import price.Price;
import tradable.BookSide;

public class CancelMessage extends Message implements Comparable<CancelMessage> {

	public CancelMessage(String user, String product, Price price, int volume,
			String details, BookSide side, String id)
			throws InvalidMessageException {
		super(user, product, price, volume, details, side, id);
	}

	@Override
	public int compareTo(CancelMessage message) {
		return this.getPrice().compareTo(message.getPrice());
	}

	@Override
	public boolean isCancel() {
		return true;
	}
}
