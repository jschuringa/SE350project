package book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import messages.CancelMessage;
import messages.FillMessage;
import messages.MarketState;
import exception.DataValidationException;
import exception.InvalidBookOperation;
import exception.InvalidMessageException;
import exception.InvalidTradableOperation;
import exception.OrderNotFoundException;
import price.Price;
import price.PriceFactory;
import publishers.CurrentMarketPublisher;
import publishers.LastSalePublisher;
import publishers.MarketDataDTO;
import publishers.MessagePublisher;
import tradable.BookSide;
import tradable.Order;
import tradable.Quote;
import tradable.Tradable;
import tradable.TradableDTO;

public class ProductBook {
	private final String product;
	private final ProductBookSide buy;
	private final ProductBookSide sell;
	private volatile String lastCurrentMarket;
	private volatile HashSet<String> userQuotes = new HashSet<>();
	private volatile static HashMap<Price, ArrayList<Tradable>> oldEntries = new HashMap< Price, ArrayList<Tradable>>();
	
	ProductBook(String product) throws InvalidBookOperation{
		this.product = setProduct(product);
		this.buy = new ProductBookSide(this, BookSide.BUY);
		this.sell = new ProductBookSide(this, BookSide.SELL);
	}
	
	private String setProduct(String productName) throws InvalidBookOperation {
		Pattern p = Pattern.compile("[a-zA-Z]{1,4}");
		Matcher m = p.matcher(productName);
		if(productName != null && m.matches() ){
			return productName;
		}
		else{
			throw new InvalidBookOperation(productName + " is invalid");
		}
	}
	
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName){
		ArrayList<TradableDTO> temp = new ArrayList<TradableDTO>();
		temp.addAll(buy.getOrdersWithRemainingQty(userName));
		temp.addAll(sell.getOrdersWithRemainingQty(userName));
		return temp;
	}
	
	public synchronized void checkTooLateToCancel(String orderId) throws OrderNotFoundException{
		for (ArrayList<Tradable> list : oldEntries.values()){
			for (Tradable t : list){
				if(t.getId().equals(orderId) && !t.isQuote()){
					try {
						MessagePublisher.getInstance().publishCancel(new CancelMessage(
								t.getUser(), t.getProduct(), t.getPrice(), t.getRemainingVolume(), 
									"Too Late to Cancel", t.getSide(), t.getId()));
					} catch (InvalidMessageException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		throw new OrderNotFoundException(orderId + " cannot be found");
	}
	
	public synchronized String[ ][ ] getBookDepth(){
		String[][] bd = new String[2][];
		bd[0] = buy.getBookDepth();
		bd[1] = sell.getBookDepth();
		return bd;
	}
	
	public synchronized MarketDataDTO getMarketData(){
		Price bestBuy = buy.topOfBookPrice();
		if(bestBuy == null){
			bestBuy = PriceFactory.makeLimitPrice(0);
		}
		Price bestSell = sell.topOfBookPrice();
		if(bestSell == null){
			bestSell = PriceFactory.makeLimitPrice(0);
		}
		int bestBuyVol = buy.topOfBookVolume();
		int bestSellVol = sell.topOfBookVolume();
		return new MarketDataDTO(this.product, bestBuy, bestBuyVol, bestSell, bestSellVol);
	}
	
	public synchronized static void addOldEntry(Tradable t){
		if(!oldEntries.containsKey(t.getPrice())){
			oldEntries.put(t.getPrice(), new ArrayList<Tradable>());
		}
		int temp = t.getRemainingVolume();
		try {
			t.setRemainingVolume(0);
			t.setCancelledVolume(temp);
		} catch (InvalidTradableOperation e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldEntries.get(t.getPrice()).add(t);
	}
	
	public synchronized void openMarket() throws InvalidMessageException, InvalidTradableOperation{
		Price buyPrice = buy.topOfBookPrice();
		if(buyPrice == null){
			return;
		}
		Price sellPrice = sell.topOfBookPrice();
		if(sellPrice == null){
			return;
		}
		while(buyPrice.greaterOrEqual(sellPrice) || buyPrice.isMarket() || sellPrice.isMarket()){
			ArrayList<Tradable> topOfBuySide = buy.getEntriesAtPrice(buyPrice);
			HashMap<String, FillMessage> allFills = null;
			ArrayList<Tradable> toRemove = new ArrayList<Tradable>();
			for(Tradable t : topOfBuySide){
				allFills = sell.tryTrade(t);
				if(t.getRemainingVolume() == 0){
					toRemove.add(t);
				}
			}
			for(Tradable t : toRemove){
				buy.removeTradable(t);
			}
			updateCurrentMarket();
			Price lastSalePrice = determineLastSalePrice(allFills);
			int lastSaleVolume = determineLastSaleQuantity(allFills);
			LastSalePublisher.getInstance().publishLastSale(product, lastSalePrice, lastSaleVolume);
			buyPrice = buy.topOfBookPrice();
			if(buyPrice == null){
				return;
			}
			sellPrice = sell.topOfBookPrice();
			if(sellPrice == null){
				return;
			}
		}
	}
	
	public synchronized void closeMarket() throws OrderNotFoundException{
		buy.cancelAll();
		sell.cancelAll();
		updateCurrentMarket();
	}
	
	public synchronized void cancelOrder(BookSide side, String orderId) throws OrderNotFoundException{
		if(side == BookSide.BUY){
			buy.submitOrderCancel(orderId);
		}
		else{
			sell.submitOrderCancel(orderId);
		}
		updateCurrentMarket();
	}
	
	public synchronized void cancelQuote(String userName){
		buy.submitQuoteCancel(userName);
		sell.submitQuoteCancel(userName);
		updateCurrentMarket();
	}
	
	public synchronized void addToBook(Quote q) throws DataValidationException, InvalidTradableOperation, InvalidMessageException{
		if(q.getQuoteSide(BookSide.SELL).getPrice().lessOrEqual(q.getQuoteSide(BookSide.BUY).getPrice())){
			throw new DataValidationException("Quote " + q.toString() + "sell side is less or equal to buy side");
		}
		if(q.getQuoteSide(BookSide.SELL).getOriginalVolume() <= 0 || q.getQuoteSide(BookSide.BUY).getOriginalVolume() <= 0){
			throw new DataValidationException("Quote has zero or negative volume");
		}
		if (userQuotes.contains(q.getUserName())){
			buy.removeQuote(q.getUserName());
			sell.removeQuote(q.getUserName());
		}
		updateCurrentMarket();
		addToBook(BookSide.BUY, q.getQuoteSide(BookSide.BUY));
		addToBook(BookSide.SELL, q.getQuoteSide(BookSide.SELL));
		userQuotes.add(q.getUserName());
		updateCurrentMarket();
	}
	
	public synchronized void addToBook(Order o) throws InvalidMessageException, InvalidTradableOperation{
		addToBook(o.getSide(), o);
		updateCurrentMarket();
	}
	
	public synchronized void updateCurrentMarket(){
		StringBuilder temp = new StringBuilder();
		Price b;
		Price s;
		if(buy.topOfBookPrice() == null){
			b = PriceFactory.makeLimitPrice(0);
			temp.append(b.toString());	
		}
		else{
			b = buy.topOfBookPrice();
			temp.append(b.toString());
		}
		temp.append(buy.topOfBookVolume());
		if(sell.topOfBookPrice() == null){
			s = PriceFactory.makeLimitPrice(0);
			temp.append(s.toString());
		}
		else{
			s = sell.topOfBookPrice();
			temp.append(s.toString());
		}
		temp.append(sell.topOfBookVolume());
		if(lastCurrentMarket == null || !lastCurrentMarket.equals(temp.toString())){
			CurrentMarketPublisher.getInstance().publishCurrentMarket(new MarketDataDTO(
					this.product, b, buy.topOfBookVolume(),
					s, sell.topOfBookVolume()));
			lastCurrentMarket = temp.toString();
		}
	}
	
	private synchronized Price determineLastSalePrice(HashMap<String, FillMessage> fills){
		if(fills == null || fills.isEmpty()){
			return null;
		}
		ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
		Collections.sort(msgs);
		Collections.reverse(msgs);
		return msgs.get(0).getPrice();
	}
	
	private synchronized int determineLastSaleQuantity(HashMap<String, FillMessage> fills){
		if(fills == null || fills.isEmpty()){
			return 0;
		}
		ArrayList<FillMessage> msgs = new ArrayList<>(fills.values());
		Collections.sort(msgs);
		return msgs.get(0).getVolume();
	}
	
	private synchronized void addToBook(BookSide side, Tradable trd) throws InvalidMessageException, InvalidTradableOperation{
		if(ProductService.getInstance().getMarketState() == MarketState.PREOPEN){
			if(side == BookSide.BUY){
				buy.addToBook(trd);
			}
			else{
				sell.addToBook(trd);
			}
			return;
		}
		HashMap<String, FillMessage> allFills = null;
		if(side == BookSide.BUY){
			allFills = sell.tryTrade(trd);
		}
		else{
			allFills = buy.tryTrade(trd);
		}
		if(!allFills.isEmpty() && allFills != null){
			updateCurrentMarket();
			int quantity = trd.getOriginalVolume() - trd.getRemainingVolume();
			Price lastSalePrice = determineLastSalePrice(allFills);
			LastSalePublisher.getInstance().publishLastSale(product, lastSalePrice, quantity);
		}
		if(trd.getRemainingVolume() > 0){
			if(trd.getPrice().isMarket()){
				MessagePublisher.getInstance().publishCancel(new CancelMessage(trd.getUser(), trd.getProduct(), trd.getPrice(),
						trd.getRemainingVolume(), "Cancelled", trd.getSide(), trd.getId()));
			}
			else{
				if(side == BookSide.BUY){
					buy.addToBook(trd);
				}
				else{
					sell.addToBook(trd);
				}
			}
		}
	}
}
