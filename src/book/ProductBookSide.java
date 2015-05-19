package book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import price.Price;
import tradable.BookSide;
import tradable.Order;
import tradable.Tradable;
import tradable.TradableDTO;

public class ProductBookSide {
	private BookSide side = null;
	private volatile static HashMap<Price, ArrayList<Tradable>> bookEntries = new HashMap<Price, ArrayList<Tradable>>();
	private ProductBook parent;
	
	
	ProductBookSide(ProductBook book, BookSide side){
		this.parent = book;
		this.side = side;
		TradeProcessor tProcessor = new TradeProcessorPriceTimeImpl TradeProcessorFactory.makeTradeProcessor(); 
		
	}
	
	
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName){
		ArrayList<TradableDTO> newTradable = new ArrayList<TradableDTO>();
		for (ArrayList<Tradable> list : bookEntries.values()){
			for(Tradable t : list){
				if(t.getUser() == userName){
					if (t.getRemainingVolume() > 0){
						TradableDTO tdto = new TradableDTO(t.getProduct(), t.getPrice(), t.getOriginalVolume(), 
								t.getRemainingVolume(), t.getCancelledVolume(), t.getUser(), t.getSide(), t.isQuote(), t.getId());
						newTradable.add(tdto);
					}
				}
			}
		}
		return newTradable;
		
	}
	
	synchronized ArrayList<Tradable> getEntriesAtTopOfBook(){
		if(bookEntries.isEmpty()){
			return null;
		}
		ArrayList<Price>sorted = new ArrayList<Price>(bookEntries.keySet());
		Collections.sort(sorted);
		if (side == BookSide.BUY){
			Collections.reverse(sorted);
		}
		return bookEntries.get(sorted.get(0));
	}
	
	
	public synchronized String[] getBookDepth(){
		if (bookEntries.isEmpty()){
			return new String[]{"<Empty>"};
		}
		int volumeSum = 0;
		String[] priceByVolume = new String[bookEntries.size()];
		ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet());
		Collections.sort(sorted);
		if (side == BookSide.BUY){
			Collections.reverse(sorted);
		}
		for (Price p : sorted){
			ArrayList<Tradable> temp = bookEntries.get(p);
			for (Tradable t : temp){
				volumeSum += t.getRemainingVolume();
			}
			String priceVolume = p + " x " + volumeSum;
			priceByVolume[] = priceVolume; //It's late, I'm tired, can't think how to get this work
			
		}
		return priceByVolume;
	}
	
	
	
	synchronized ArrayList<Tradable> getEntriesAtPrice(Price price){
		if (!bookEntries.containsKey(price)){
			return null;
		}
		return bookEntries.get(price);
	}
	
	public synchronized boolean hasMarketPrice(){
		if (bookEntries.containsKey()){
			return true;					//Needs to check if contains market price
		}
		return false;
	}

	
	public synchronized boolean hasOnlyMarketPrice(){
		if ((bookEntries.size() == 1) && (bookEntries.containsKey())){   //Needs to get market price
			return true;
		}
		return false;
	}
	
	
	public synchronized Price topOfBookPrice(){
		if (bookEntries.isEmpty()){
			return null;
		}
		ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet());
		Collections.sort(sorted);
		if (side == BookSide.BUY){
			Collections.reverse(sorted);
		}
		return sorted.get(0);
	}
	
	
	
	public synchronized int topOfBookVolume(){
		if (bookEntries.isEmpty()){
			return 0;
		}
		int sum = 0;
		ArrayList<Price> sorted = new ArrayList<Price>(bookEntries.keySet());
		Collections.sort(sorted);
		if (side == BookSide.BUY){
			Collections.reverse(sorted);
		}
		Price first = sorted.get(0);
		ArrayList<Tradable> firstList = bookEntries.get(first);
		for (Tradable t : firstList){
			sum += t.getRemainingVolume();
		}
		return sum;
	}
	
	public synchronized boolean isEmpty(){
		if (bookEntries.size() == 0){
			return true;
		}
		return false;
	}
	
}
