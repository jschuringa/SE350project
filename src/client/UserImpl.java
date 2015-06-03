package client;

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
import gui.UserDisplayManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import messages.CancelMessage;
import messages.FillMessage;
import price.Price;
import tradable.BookSide;
import tradable.TradableDTO;

import java.sql.Timestamp;

public class UserImpl implements User {
	private final String userName;
	private long connectionId;
	ArrayList<String> stocks;
	ArrayList<TradableUserData> orders = new ArrayList<TradableUserData>();
	Position position;
	UserDisplayManager display;
	
	public UserImpl(String uName) throws DataValidationException{
		userName = setUserName(uName);
		position = new Position();
	}
	
	private String setUserName(String userName) throws DataValidationException{
		if(userName == null)
			throw new DataValidationException("User cannot be null");
		Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
		Matcher m = p.matcher(userName);
		if(!m.matches() ){
			throw new DataValidationException("User name invalid: " + userName);
		}
		return userName;
	}
	
	private void isConnected() throws UserNotConnectedException{
		if(Long.valueOf(connectionId) == null){
			throw new UserNotConnectedException(userName + " is not connected");
		}
	}
	
	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public void acceptLastSale(String product, Price p, int v) {
		//isConnected();
		try {
			display.updateLastSale(product, p, v);
		} catch (InvalidPriceOperation e1) {
			System.out.println(e1.getMessage());
		}
		try {
			position.updateLastSale(product, p);
		} catch (DataValidationException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void acceptMessage(FillMessage fm) {
		if(fm == null){
			return;
		}
		Timestamp t = new Timestamp(System.currentTimeMillis());
		StringBuilder summary = new StringBuilder();
		summary.append("{");
		summary.append(t.toString());
		summary.append("} Fill Message: ");
		summary.append(fm.getBookSide().toString());
		summary.append(" ");
		summary.append(fm.getVolume());
		summary.append(" ");
		summary.append(fm.getProduct());
		summary.append(" at ");
		summary.append(fm.getPrice().toString());
		summary.append(" ");
		summary.append(fm.getDetails());
		summary.append(" [Tradable Id: ");
		summary.append(fm.getId());
		summary.append("]");
		summary.append("\n");
		display.updateMarketActivity(summary.toString());
		try {
			position.updatePosition(fm.getProduct(), fm.getPrice(), fm.getBookSide(), fm.getVolume());
		} catch (DataValidationException e) {
			System.out.println(e.getMessage());
		} catch (InvalidPriceOperation e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void acceptMessage(CancelMessage cm) {
		if(cm == null){
			return;
		}
		Timestamp t = new Timestamp(System.currentTimeMillis());
		StringBuilder summary = new StringBuilder();
		summary.append("{");
		summary.append(t.toString());
		summary.append("} Cancel Message: ");
		summary.append(cm.getBookSide().toString());
		summary.append(" ");
		summary.append(cm.getVolume());
		summary.append(" ");
		summary.append(cm.getProduct());
		summary.append(" at ");
		summary.append(cm.getPrice().toString());
		summary.append(" ");
		summary.append(cm.getDetails());
		summary.append(" [Tradable Id: ");
		summary.append(cm.getId());
		summary.append("]");
		summary.append("\n");
		display.updateMarketActivity(summary.toString());
	}

	@Override
	public void acceptMarketMessage(String message) {
		display.updateMarketState(message);
	}

	@Override
	public void acceptTicker(String product, Price p, char direction) {
		display.updateTicker(product, p, direction);
	}

	@Override
	public void acceptCurrentMarket(String product, Price bp, int bv, Price sp,
			int sv) {
		try {
			display.updateMarketData(product, bp, bv, sp, sv);
		} catch (InvalidPriceOperation e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void connect() throws DataValidationException, UserNotConnectedException, AlreadyConnectedException, InvalidConnectionIdException {
		connectionId = UserCommandService.getInstance().connect(this);
		stocks = UserCommandService.getInstance().getProducts(userName, connectionId);
		display = new UserDisplayManager(this);
	}

	@Override
	public void disconnect() throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException {
		UserCommandService.getInstance().disConnect(userName, connectionId);
	}

	@Override
	public void showMarketDisplay() throws Exception {
		if(stocks == null){
			throw new UserNotConnectedException();
		}
		if(display == null){
			display = new UserDisplayManager(this);
		}
		display.showMarketDisplay();
	}

	@Override
	public String submitOrder(String product, Price price, int volume,
			BookSide side) throws InvalidTradableOperation, UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, InvalidMessageException, DataValidationException, OrderNotFoundException {
		String id = UserCommandService.getInstance().submitOrder(userName, connectionId, product, price, volume, side);
		TradableUserData tud = new TradableUserData(userName, product, id, side);
		orders.add(tud);
		return id;
	}

	@Override
	public void submitOrderCancel(String product, BookSide side, String orderId) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, OrderNotFoundException, InvalidMessageException, DataValidationException {
		UserCommandService.getInstance().submitOrderCancel(userName, connectionId, product, side, orderId);
		
	}

	@Override
	public void submitQuote(String product, Price buyPrice, int buyVolume,
			Price sellPrice, int sellVolume) throws UserNotConnectedException, InvalidConnectionIdException, InvalidTradableOperation, InvalidMarketStateException, NoSuchProductException, DataValidationException, InvalidMessageException {
		UserCommandService.getInstance().submitQuote(userName, connectionId, product, buyPrice, buyVolume, sellPrice, sellVolume);
	}

	@Override
	public void submitQuoteCancel(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidMarketStateException, NoSuchProductException, DataValidationException {
		UserCommandService.getInstance().submitQuoteCancel(userName, connectionId, product);
	}
	
	@Override
	public void subscribeCurrentMarket(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException{
		UserCommandService.getInstance().subscribeCurrentMarket(userName, connectionId, product);
	}

	@Override
	public void subscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException {
		UserCommandService.getInstance().subscribeLastSale(userName, connectionId, product);
	}

	@Override
	public void subscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException {
		UserCommandService.getInstance().subscribeMessages(userName, connectionId, product);
	}

	@Override
	public void subscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdException, AlreadySubscribedException, InvalidSubscriptionException, DataValidationException {
		UserCommandService.getInstance().subscribeTicker(userName, connectionId, product);
	}
	
	@Override
	public void unSubscribeCurrentMarket(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException{
		UserCommandService.getInstance().unSubscribeCurrentMarket(userName, connectionId, product);
	}

	@Override
	public void unSubscribeLastSale(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException{
		UserCommandService.getInstance().unSubscribeLastSale(userName, connectionId, product);
	}
	
	@Override
	public void unSubscribeMessages(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException{
		UserCommandService.getInstance().unSubscribeMessages(userName, connectionId, product);
	}
	
	@Override
	public void unSubscribeTicker(String product) throws UserNotConnectedException, InvalidConnectionIdException, InvalidSubscriptionException, NotSubscribedException, DataValidationException{
		UserCommandService.getInstance().unSubscribeTicker(userName, connectionId, product);
	}
	
	@Override
	public Price getAllStockValue() throws InvalidPriceOperation, DataValidationException {
		return position.getAllStockValue();
	}

	@Override
	public Price getAccountCosts() {
		return position.getAccountCosts();
	}

	@Override
	public Price getNetAccountValue() throws InvalidPriceOperation, DataValidationException {
		return position.getNetAccountValue();
	}

	@Override
	public String[][] getBookDepth(String product) throws UserNotConnectedException, InvalidConnectionIdException, NoSuchProductException, DataValidationException {
		return UserCommandService.getInstance().getBookDepth(userName, connectionId, product);
	}

	@Override
	public String getMarketState() throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException {
		return UserCommandService.getInstance().getMarketState(userName, connectionId);
	}

	@Override
	public ArrayList<TradableUserData> getOrderIds() {
		return new ArrayList<TradableUserData>(orders);
	}

	@Override
	public ArrayList<String> getProductList() {
		return new ArrayList<String>(stocks);
	}

	@Override
	public Price getStockPositionValue(String sym) throws DataValidationException, InvalidPriceOperation {
		return position.getStockPositionValue(sym);
	}

	@Override
	public int getStockPositionVolume(String product) throws DataValidationException {
		return position.getStockPositionVolume(product);
	}

	@Override
	public ArrayList<String> getHoldings() {
		return position.getHoldings();
	}

	@Override
	public ArrayList<TradableDTO> getOrdersWithRemainingQty(String product) throws UserNotConnectedException, InvalidConnectionIdException, DataValidationException {
		return UserCommandService.getInstance().getOrdersWithRemainingQty(userName, connectionId, product);
	}

}
