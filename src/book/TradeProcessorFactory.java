package book;

import exception.InvalidBookOperation;

public class TradeProcessorFactory {
	public static TradeProcessor makeTradeProcessor(ProductBookSide side) throws InvalidBookOperation{
		return new TradeProcessorPriceTimeImpl(side);
	}
}
