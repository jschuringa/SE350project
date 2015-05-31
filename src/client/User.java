package client;

import java.util.ArrayList;

import messages.CancelMessage;
import messages.FillMessage;
import price.Price;
import tradable.BookSide;
import tradable.TradableDTO;

public interface User {
	
	public String getUserName();
	
	public void acceptLastSale(String product, Price p, int v);
	
	public void acceptMessage(FillMessage fm);
	
	public void acceptMessage(CancelMessage cm);
	
	public void acceptMarketMessage(String message);
	
	public void acceptTicker(String product, Price p, char direction);
	
	public void acceptCurrentMarket(String product, Price bp, int bv, Price sp, int sv);
	
	public void connect();
	
	public void disconnect();
	
	public void showMarketDisplay();
	
	public String submitOrder(String product, Price price, int volume, BookSide side);
	
	public void submitOrderCancel(String product, BookSide side, String orderId);
	
	public void submitQuote(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume);
	
	public void submitQuoteCancel(String product);
	
	public void subscribeLastSale(String product);
	
	public void subscribeMessages(String product);
	
	public void subscribeTicker(String product);
	
	public Price getAllStockValue();
	
	public Price getAccountCosts();
	
	public Price getNetAccountValue();
	
	public String[][] getBookDepth(String product);
	
	public String getMarketState();
	
	public ArrayList<TradableUserData> getOrderIds();
	
	public ArrayList<String> getProductList();
	
	public Price getStockPositionValue(String sym);
	
	public int getStockPositionVolume(String product);
	
	public ArrayList<String> getHoldings();
	
	public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product);
	
}
