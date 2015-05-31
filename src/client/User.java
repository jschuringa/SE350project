package client;

import java.util.ArrayList;

import exception.AlreadyConnectedException;
import exception.AlreadySubscribedException;
import exception.DataValidationException;
import exception.InvalidConnectionIdException;
import exception.InvalidMarketStateException;
import exception.InvalidMessageException;
import exception.InvalidPriceOperation;
import exception.InvalidSubscriptionException;
import exception.InvalidTradableOperation;
import exception.NoSuchProductException;
import exception.NotSubscribedException;
import exception.OrderNotFoundException;
import exception.UserNotConnectedException;
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
	
	public void connect() throws DataValidationException, UserNotConnectedException, AlreadyConnectedException, InvalidConnectionIdException;
	
	public void disconnect() throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException;
	
	public void showMarketDisplay() throws Exception;
	
	public String submitOrder(String product, Price price, int volume, BookSide side) throws InvalidTradableOperation, UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, InvalidMessageException, DataValidationException, OrderNotFoundException;
	
	public void submitOrderCancel(String product, BookSide side, String orderId) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, OrderNotFoundException, InvalidMessageException, DataValidationException;
	
	public void submitQuote(String product, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws UserNotConnectedException, InvalidConnectionIdException, InvalidTradableOperation, InvalidMarketStateException, NoSuchProductException, DataValidationException, InvalidMessageException;
	
	public void submitQuoteCancel(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, DataValidationException;
	
	public void subscribeCurrentMarket(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException;
	
	public void subscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException;
	
	public void subscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException;
	
	public void subscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException;
	
	public void unSubscribeCurrentMarket(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException;
	
	public void unSubscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException;
	
	public void unSubscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException;
	
	public void unSubscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException;
	
	public Price getAllStockValue() throws InvalidPriceOperation, DataValidationException;
	
	public Price getAccountCosts();
	
	public Price getNetAccountValue() throws InvalidPriceOperation, DataValidationException;
	
	public String[][] getBookDepth(String product) throws UserNotConnectedException, InvalidConnectionIdException, NoSuchProductException, DataValidationException;
	
	public String getMarketState() throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException;
	
	public ArrayList<TradableUserData> getOrderIds();
	
	public ArrayList<String> getProductList();
	
	public Price getStockPositionValue(String sym) throws DataValidationException, InvalidPriceOperation;
	
	public int getStockPositionVolume(String product) throws DataValidationException;
	
	public ArrayList<String> getHoldings();
	
	public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException;
	
}
