package book;

import java.util.ArrayList;
import java.util.HashMap;

import price.Price;
import exception.InvalidMessageException;
import messages.FillMessage;
import tradable.Tradable;
import price.Price;

public class TradeProcessorPriceTimeImpl implements TradeProcessor {
	private HashMap<String, FillMessage> fillMessages = new HashMap<String, FillMessage>();
	private ProductBookSide instance;
	
	public TradeProcessorPriceTimeImpl(ProductBookSide instance1){
		this.instance = instance1;
	}
	
	private String makeFillKey(FillMessage fm){
		String key = fm.getUser() + fm.getId() + fm.getPrice();
		return key;
	}
	
	private boolean isNewFill(FillMessage fm){
		String key = makeFillKey(fm);
		if (!(fillMessages.containsKey(key))){
			return true;
		}
		FillMessage oldkey = fillMessages.get(key);
		if (oldkey != fm){
			return true;
		}
		else if(oldkey.getId() != fm.getId()){
			return true;
		}
		return false;
	}
	

	private void addFillMessage(FillMessage fm) throws InvalidMessageException{
		if (isNewFill(fm) == true){
			String key = makeFillKey(fm);
			fillMessages.put(key, fm);
		} else {
			String key = makeFillKey(fm);
			FillMessage oldfill = fillMessages.get(key);
			try {
				oldfill.setVolume(fm.getVolume() + oldfill.getVolume());
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
				e.printStackTrace();
			}
			try {
				oldfill.setDetails(fm.getDetails());
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());;
			}
		}
	}

	@Override
	public HashMap<String, FillMessage> doTrade(Tradable trd) {
		Price tPrice = null;
		fillMessages = new HashMap<String, FillMessage>();
		ArrayList<Tradable> tradedOut = new ArrayList<Tradable>();
		ArrayList<Tradable> entriesAtPrice = ProductBookSide.getEntriesAtTopOfBook();
		for (Tradable t : entriesAtPrice){
			if (trd.getRemainingVolume() == 0){
				if (trd.getRemainingVolume() >= t.getRemainingVolume()){
					tradedOut.add(t);
					if(t.getPrice().isMarket()){
						tPrice = trd.getPrice();
					} else {
						tPrice = t.getPrice();
					}
					FillMessage tFill = new FillMessage(t.getUser(), t.getProduct(), 
							tPrice, t.getRemainingVolume(), "leaving 0", t.getSide(), t.getId());
					addFillMessage(tFill);
					
					FillMessage trdFill = new FillMessage(trd.getUser(), trd.getProduct(), tPrice, trd.getRemainingVolume(), 
							"leaving " + (trd.getRemainingVolume()-t.getRemainingVolume()), trd.getSide(), trd.getId());
					addFillMessage(trdFill);
					
					trd.setRemainingVolume(trd.getRemainingVolume()-t.getRemainingVolume());
					t.setRemainingVolume(0);
					ProductBook.addOldEntry(t);
				} else {
					int remainder = t.getRemainingVolume() - trd.getRemainingVolume();
					if(t.getPrice().isMarket()){
						tPrice = trd.getPrice();
					} else{
						tPrice = t.getPrice();
					}
					FillMessage tFill = new FillMessage(t.getUser(), t.getProduct(), tPrice, trd.getRemainingVolume(),
							"leaving " + remainder, t.getSide(), t.getId());
					addFillMessage(tFill);
					
					FillMessage trdFill = new FillMessage(trd.getUser(), trd.getProduct(), tPrice, trd.getRemainingVolume(), 
							"leaving 0", trd.getSide(), trd.getId());
					addFillMessage(trdFill);
					
					trd.setRemainingVolume(0);
					t.setRemainingVolume(remainder);
					ProductBook.addOldEntry(trd);
				}
			}
		}
		for (Tradable t : tradedOut){
			entriesAtPrice.remove(t);
			if (entriesAtPrice.isEmpty()){
				ProductBookSide.clearIfEmpty(ProductBookSide.topOfBookPrice());
			}
		}
		return fillMessages;
	}

}
