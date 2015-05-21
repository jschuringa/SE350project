package book;

import java.util.HashMap;

import exception.InvalidMessageException;
import exception.InvalidTradableOperation;
import tradable.Tradable;
import messages.FillMessage;

public interface TradeProcessor {
	
	public HashMap<String, FillMessage> doTrade(Tradable trd) throws InvalidMessageException, InvalidTradableOperation;

}
