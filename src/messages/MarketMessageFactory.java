package messages;

import exception.InvalidMessageException;

public class MarketMessageFactory {
	private static final MarketMessage closed = new MarketMessage(MarketState.CLOSED);
	private static final MarketMessage preopen = new MarketMessage(MarketState.PREOPEN);
	private static final MarketMessage open = new MarketMessage(MarketState.OPEN);
	
	public static MarketMessage makeMarketMessage(MarketState state) throws InvalidMessageException{
		if(state == null)
			throw new InvalidMessageException("State cannot be null");
		if(state.equals(MarketState.CLOSED))
			return closed;
		else if(state.equals(MarketState.PREOPEN))
			return preopen;
		else
			return open;
	}
}
