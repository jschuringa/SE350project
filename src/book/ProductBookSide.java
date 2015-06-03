package book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import messages.CancelMessage;
import messages.FillMessage;
import price.Price;
import price.PriceFactory;
import publishers.MessagePublisher;
import exception.InvalidBookOperation;
import exception.InvalidMessageException;
import exception.InvalidTradableOperation;
import exception.OrderNotFoundException;
import tradable.BookSide;
import tradable.Tradable;
import tradable.TradableDTO;

public class ProductBookSide {
	private final ProductBook parent;
	private final BookSide side;
	private HashMap<Price, ArrayList<Tradable>> bookEntries = new HashMap< Price, ArrayList<Tradable>>();
	private final TradeProcessor processor;
	
	ProductBookSide(ProductBook parent, BookSide side) throws InvalidBookOperation{
		if(side == null){
			throw new InvalidBookOperation("Side cannot be null");
			
		}
		if(parent == null){
			throw new InvalidBookOperation("Parent cannot be null");
		}
		this.side = side;
		this.parent = parent;
		this.processor = TradeProcessorFactory.makeTradeProcessor(this);
	}
	
	private synchronized ArrayList<Price> getSortedPrices(){
		if(bookEntries.isEmpty()){
			return null;
		}
		ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet()); // Get prices
		Collections.sort(sorted); // Sort them
		if (side == BookSide.BUY) {
			Collections.reverse(sorted);
		}
		return sorted;
	}
	
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName){
		ArrayList<TradableDTO> temp = new ArrayList<TradableDTO>();
		for (ArrayList<Tradable> list : bookEntries.values()){
			for (Tradable t : list){
				if(t.getUser().equals(userName) && t.getRemainingVolume() > 0 && !t.isQuote()){
					temp.add(new TradableDTO(t.getProduct(), t.getPrice(), 
							t.getOriginalVolume(), t.getRemainingVolume(), t.getCancelledVolume(),
								t.getUser(), t.getSide(), t.isQuote(), t.getId()));
				}
			}
		}
		return temp;
	}
	
	synchronized ArrayList<Tradable> getEntriesAtTopOfBook(){
		if(bookEntries.isEmpty()){
			return null;
		}
		ArrayList<Price> sorted = getSortedPrices();
		return bookEntries.get(sorted.get(0));
	}
	
	public synchronized String[] getBookDepth(){
		if(bookEntries.isEmpty()){
			return new String[]{"<Empty>"};
		}
		String[] depth = new String[bookEntries.size()];
		ArrayList<Price> sorted = getSortedPrices();
		int index = 0;
		for(Price p : sorted){
			ArrayList<Tradable> trades = bookEntries.get(p);
			int remaining = 0;
			for(Tradable t : trades){
				remaining += t.getRemainingVolume();
			}
			StringBuilder temp = new StringBuilder(p.toString());
			temp.append(" x ");
			temp.append(remaining);
			depth[index] = temp.toString();
			index++;
		}
		return depth;
	}
	
	synchronized ArrayList<Tradable> getEntriesAtPrice(Price price){
		if(bookEntries.containsKey(price)){
			return bookEntries.get(price);
		}
		else{
			return null;
		}
	}
	
	public synchronized boolean hasMarketPrice(){
		if(bookEntries.containsKey(PriceFactory.makeMarketPrice())){
			return true;
		}
		else{
			return false;
		}
	}
	
	public synchronized boolean hasOnlyMarketPrice(){
		if(bookEntries.containsKey(PriceFactory.makeMarketPrice()) && bookEntries.size() == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public synchronized Price topOfBookPrice(){
		if(bookEntries.isEmpty()){
			return null;
		}
		ArrayList<Price> sorted = getSortedPrices();
		return sorted.get(0);
	}
	
	public synchronized int topOfBookVolume(){
		if(bookEntries.isEmpty()){
			return 0;
		}
		ArrayList<Price> sorted = getSortedPrices();
		ArrayList<Tradable> trades = bookEntries.get(sorted.get(0));
		int remaining = 0;
		for(Tradable t : trades){
			remaining += t.getRemainingVolume();
		}
		return remaining;
	}
	
	public synchronized boolean isEmpty(){
		return bookEntries.isEmpty();
	}
	
	public synchronized void cancelAll() throws OrderNotFoundException{
		ArrayList<String> quotes = new ArrayList<String>();
		ArrayList<String> orders = new ArrayList<String>();
		for (ArrayList<Tradable> list : bookEntries.values()){
			for (Tradable t : list){
				if(t.isQuote()){
					quotes.add(t.getUser());
				}
				else{
					orders.add(t.getId());
				}
			}
		}
		for(String s : quotes)
			submitQuoteCancel(s);
		for(String s : orders)
			submitOrderCancel(s);
	}
	
	public synchronized TradableDTO removeQuote(String user){
		for (ArrayList<Tradable> list : bookEntries.values()){
			for (Tradable t : list){
				if(t.getUser().equals(user) && t.isQuote()){
					list.remove(t);
					clearIfEmpty(t.getPrice());
					return new TradableDTO(t.getProduct(), t.getPrice(), 
							t.getOriginalVolume(), t.getRemainingVolume(), t.getCancelledVolume(),
								t.getUser(), t.getSide(), t.isQuote(), t.getId());
				}
			}
		}
		return null;
	}
	
	public synchronized void submitOrderCancel(String orderId) throws OrderNotFoundException{
		for (ArrayList<Tradable> list : bookEntries.values()){
			for (Tradable t : list){
				if(t.getId().equals(orderId) && !t.isQuote()){
					list.remove(t);
					clearIfEmpty(t.getPrice());
					try {
						MessagePublisher.getInstance().publishCancel(new CancelMessage(t.getUser(), t.getProduct(), t.getPrice(), 
								t.getRemainingVolume(), "Order " + t.getSide().toString() + "-Side Cancelled", t.getSide(), orderId));
					} catch (InvalidMessageException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					addOldEntry(t);
					return;
				}
			}
		}
		parent.checkTooLateToCancel(orderId);
	}
	
	public synchronized void submitQuoteCancel(String userName){
		TradableDTO dto = removeQuote(userName);
		if(dto == null){
			return;
		}
		try {
			MessagePublisher.getInstance().publishCancel(new CancelMessage(dto.user, dto.product, dto.price, 
					dto.remainingVolume, "Quote " + dto.side.toString() + "-Side Cancelled", dto.side, dto.id));
		} catch (InvalidMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void addOldEntry(Tradable t){
		parent.addOldEntry(t);
	}
	
	public synchronized void addToBook(Tradable trd){
		if(!bookEntries.containsKey(trd.getPrice())){
			bookEntries.put(trd.getPrice(), new ArrayList<Tradable>());
		}
		bookEntries.get(trd.getPrice()).add(trd);
	}
	
	public HashMap<String, FillMessage> tryTrade(Tradable trd) throws InvalidMessageException, InvalidTradableOperation{
		HashMap<String, FillMessage> allFills = new HashMap<String, FillMessage>();
		if(side == BookSide.BUY){
			allFills = trySellAgainstBuySideTrade(trd);
		}
		else{
			allFills = tryBuyAgainstSellSideTrade(trd);
		}
		for(FillMessage f : allFills.values()){
			MessagePublisher.getInstance().publishFill(f);
		}
		return allFills;
	}
	
	public synchronized HashMap<String, FillMessage> trySellAgainstBuySideTrade(Tradable trd) throws InvalidMessageException, InvalidTradableOperation{
		HashMap<String, FillMessage> allFills = new HashMap<String, FillMessage>();
		HashMap<String, FillMessage> fillMsgs = new HashMap<String, FillMessage>();
		while(trd.getRemainingVolume() > 0 && !isEmpty() && 
				(trd.getPrice().isMarket() || trd.getPrice().lessOrEqual(topOfBookPrice()))){
			HashMap<String, FillMessage> someMsgs = processor.doTrade(trd);
			fillMsgs = mergeFills(fillMsgs, someMsgs);
		}
		allFills.putAll(fillMsgs);
		return allFills;
	}
	
	private HashMap<String, FillMessage> mergeFills(HashMap<String, FillMessage> existing, HashMap<String,
			FillMessage> newOnes){
		if(existing.isEmpty()){
			return new HashMap<String, FillMessage>(newOnes);
		}
		HashMap<String, FillMessage> results = new HashMap<String, FillMessage>(existing);
		for (String key : newOnes.keySet()) { // For each Trade Id key in the “newOnes” HashMap
			if (!existing.containsKey(key)) { // If the “existing” HashMap does not have that key…
				results.put(key, newOnes.get(key)); // …then simply add this entry to the “results” HashMap
			} else { // Otherwise, the “existing” HashMap does have that key – we need to update the data
				FillMessage fm = results.get(key); // Get the FillMessage from the “results” HashMap
			// NOTE – for the below, you will need to make these 2 FillMessage methods “public”!
				try {
					fm.setVolume(newOnes.get(key).getVolume());// + results.get(key).getVolume());
					fm.setDetails(newOnes.get(key).getDetails());
				} catch (InvalidMessageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		return results;
	}
	
	public synchronized HashMap<String, FillMessage> tryBuyAgainstSellSideTrade(Tradable trd) throws InvalidMessageException, InvalidTradableOperation{
		HashMap<String, FillMessage> allFills = new HashMap<String, FillMessage>();
		HashMap<String, FillMessage> fillMsgs = new HashMap<String, FillMessage>();
		while(trd.getRemainingVolume() > 0 && !isEmpty() && 
				(trd.getPrice().isMarket() || trd.getPrice().greaterOrEqual(topOfBookPrice()))){
			HashMap<String, FillMessage> someMsgs = processor.doTrade(trd);
			fillMsgs = mergeFills(fillMsgs, someMsgs);
		}
		allFills.putAll(fillMsgs);
		return allFills;
	}
	
	public synchronized void clearIfEmpty(Price p){
		if(bookEntries.containsKey(p)){
			if(bookEntries.get(p).isEmpty()){
				bookEntries.remove(p);
			}
		}
	}
	
	public synchronized void removeTradable(Tradable t){
		ArrayList<Tradable> entries = bookEntries.get(t.getPrice());
		if(entries.isEmpty()){
			return;
		}
		boolean removed = entries.remove(t);
		if(!removed){
			return;
		}
		clearIfEmpty(t.getPrice());
		
	}
}
