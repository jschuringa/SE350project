package book;

import java.util.HashMap;

import tradable.Tradable;
import messages.FillMessage;

public interface TradeProcessor {
	
	public HashMap<String, FillMessage> doTrade(Tradable trd);

}
