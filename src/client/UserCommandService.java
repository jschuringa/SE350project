package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import price.Price;
import publishers.CurrentMarketPublisher;
import publishers.LastSalePublisher;
import publishers.MessagePublisher;
import publishers.TickerPublisher;
import tradable.BookSide;
import tradable.Order;
import tradable.Quote;
import tradable.TradableDTO;
import book.ProductService;
import exception.AlreadyConnectedException;
import exception.AlreadySubscribedException;
import exception.DataValidationException;
import exception.InvalidConnectionIdException;
import exception.InvalidMarketStateException;
import exception.InvalidMessageException;
import exception.InvalidSubscriptionException;
import exception.InvalidTradableOperation;
import exception.NoSuchProductException;
import exception.NotSubscribedException;
import exception.OrderNotFoundException;
import exception.UserNotConnectedException;

public class UserCommandService {
	private volatile HashMap<String, Long> connectedUserIds = new HashMap<String, Long>();
	private volatile HashMap<String, User> connectedUsers = new HashMap<String, User>();
	private volatile HashMap<String, Long> connectionTimes = new HashMap<String, Long>();
	private static volatile UserCommandService instance;
	
	private UserCommandService(){}
	
	public static UserCommandService getInstance(){
		if (instance == null)
			synchronized(UserCommandService.class){
				if (instance == null)
					instance = new UserCommandService();
			}
		return instance;
	}
	
	private String validateProduct(String product) throws DataValidationException{
		Pattern p = Pattern.compile("[a-zA-Z]{1,4}");
		Matcher m = p.matcher(product);
		if(product == null || !m.matches() ){
			throw new DataValidationException(product + " is invalid");
		}
		else{
			return product.toUpperCase();
		}
	}
	
	private void validateUserName(String userName) throws DataValidationException{
		if(userName == null)
			throw new DataValidationException("User cannot be null");
		Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
		Matcher m = p.matcher(userName);
		if(!m.matches() ){
			throw new DataValidationException("User name invalid: " + userName);
		}
	}
	
	private void verifyUser(String userName, long connId) throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException{
		validateUserName(userName);
		if(!connectedUserIds.containsKey(userName)){
			throw new UserNotConnectedException(userName);
		}
		if(connectedUserIds.get(userName) != connId){
			throw new InvalidConnectionIdException();
		}
	}
	
	public synchronized long connect(User user) throws DataValidationException, AlreadyConnectedException{
		if(user == null){
			throw new DataValidationException("User cannot be null");
		}
		String userName = user.getUserName();
		validateUserName(userName);
		if(connectedUserIds.containsKey(userName)){
			throw new AlreadyConnectedException(userName);
		}
		connectedUserIds.put(userName, System.nanoTime());
		connectedUsers.put(userName, user);
		connectionTimes.put(userName, System.currentTimeMillis());
		return connectedUserIds.get(userName);
	}
	
	public synchronized void disConnect(String userName, long connId) throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException{
		verifyUser(userName, connId);
		connectedUserIds.remove(userName);
		connectedUsers.remove(userName);
		connectionTimes.remove(userName);
	}
	
	public String[][] getBookDepth(String userName, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, NoSuchProductException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		product = validateProduct(product);
		return ProductService.getInstance().getBookDepth(product);
	}
	
	public String getMarketState(String userName, long connId) throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		return ProductService.getInstance().getMarketState().toString();
	}
	
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException{
		verifyUser(userName, connId);
		product = validateProduct(product);
		return ProductService.getInstance().getOrdersWithRemainingQty(userName, product);
	}
	
	public ArrayList<String> getProducts(String userName, long connId) throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		ArrayList<String> products = ProductService.getInstance().getProductList();
		Collections.sort(products);
		return products;
	}
	
	public String submitOrder(String userName, long connId, String product, Price price, int volume, BookSide side) throws InvalidTradableOperation, UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, InvalidMessageException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		Order o = new Order(userName, product, price, volume, side);
		ProductService.getInstance().submitOrder(o);
		return o.getId();
	}
	
	public void submitOrderCancel(String userName, long connId, String product, BookSide side, String orderId) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, OrderNotFoundException, InvalidMessageException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		ProductService.getInstance().submitOrderCancel(product, side, orderId);
	}
	
	public void submitQuote(String userName, long connId, String product, Price bPrice, int bVolume, Price sPrice, int sVolume) throws UserNotConnectedException, InvalidConnectionIdException, InvalidTradableOperation, InvalidMarketStateException, NoSuchProductException, DataValidationException, InvalidMessageException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		Quote q = new Quote(userName, product, bPrice, bVolume, sPrice, sVolume);
		ProductService.getInstance().submitQuote(q);
	}
	
	public void submitQuoteCancel(String userName, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		ProductService.getInstance().submitQuoteCancel(userName, product);
	}
	
	public void subscribeCurrentMarket(String userName, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		CurrentMarketPublisher.getInstance().subscribe(connectedUsers.get(userName), product);
	}
	
	public void subscribeLastSale(String userName, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		LastSalePublisher.getInstance().subscribe(connectedUsers.get(userName), product);
	}
	
	public void subscribeMessages(String userName, long conn, String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException{
		synchronized(this){
			verifyUser(userName, conn);
		}
		MessagePublisher.getInstance().subscribe(connectedUsers.get(userName), product);
	}
	
	public void subscribeTicker(String userName, long conn, String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException{
		synchronized(this){
			verifyUser(userName, conn);
		}
		TickerPublisher.getInstance().subscribe(connectedUsers.get(userName), product);
	}
	
	public void unSubscribeCurrentMarket(String userName, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		CurrentMarketPublisher.getInstance().unSubscribe(connectedUsers.get(userName), product);
	}
	
	public void unSubscribeLastSale(String userName, long connId, String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException{
		synchronized(this){
			verifyUser(userName, connId);
		}
		LastSalePublisher.getInstance().unSubscribe(connectedUsers.get(userName), product);
	}
	
	public void unSubscribeMessages(String userName, long conn, String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException{
		synchronized(this){
			verifyUser(userName, conn);
		}
		MessagePublisher.getInstance().unSubscribe(connectedUsers.get(userName), product);
	}
	
	public void unSubscribeTicker(String userName, long conn, String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException{
		synchronized(this){
			verifyUser(userName, conn);
		}
		TickerPublisher.getInstance().unSubscribe(connectedUsers.get(userName), product);
	}
}
