package book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import price.Price;
import exception.InvalidBookOperation;
import exception.InvalidMessageException;
import exception.InvalidTradableOperation;
import messages.FillMessage;
import tradable.Tradable;

public class TradeProcessorPriceTimeImpl implements TradeProcessor {
	private HashMap<String, FillMessage> fillMessages;
	private final ProductBookSide side;
	
	TradeProcessorPriceTimeImpl(ProductBookSide side) throws InvalidBookOperation{
		if(side == null){
			throw new InvalidBookOperation("Trade Processor side cannot be null");
		}
		this.side = side;
	}
	
	private String makeFillKey(FillMessage fm){
		return fm.getUser() + fm.getId() + fm.getPrice().toString();
	}
	
	private boolean isNewFill(FillMessage fm){
		if(!fillMessages.containsKey(makeFillKey(fm))){
			return true;
		}
		FillMessage oldFill = fillMessages.get(makeFillKey(fm));
		if(oldFill.getBookSide() != fm.getBookSide()){
			return true;
		}
		if(!oldFill.getId().equals(fm.getId())){
			return true;
		}
		return false;
	}
	
	private void addFillMessage(FillMessage fm) throws InvalidMessageException{
		if(isNewFill(fm)){
			fillMessages.put(makeFillKey(fm), fm);
		}
		else{
			FillMessage temp = fillMessages.get(makeFillKey(fm));
			int vol = temp.getVolume() + fm.getVolume();
			fm.setVolume(vol);
			fillMessages.replace(makeFillKey(fm), fm);
		}
	}
	
	
	@Override
	public HashMap<String, FillMessage> doTrade(Tradable trd) throws InvalidMessageException, InvalidTradableOperation {
		fillMessages = new HashMap<String, FillMessage>();
		ArrayList<Tradable> tradedOut = new ArrayList<Tradable>();
		ArrayList<Tradable> entriesAtPrice = side.getEntriesAtTopOfBook();
		for(Tradable t : entriesAtPrice){
			if(trd.getRemainingVolume() != 0){
				if(trd.getRemainingVolume() >= t.getRemainingVolume()){
					tradedOut.add(t);
					Price tPrice;
					if(t.getPrice().isMarket()){
						tPrice = trd.getPrice();
					}
					else{
						tPrice = t.getPrice();
					}
					FillMessage fm1 = new FillMessage(t.getUser(), t.getProduct(), 
							tPrice, t.getRemainingVolume(), "leaving 0", t.getSide(), t.getId());
					addFillMessage(fm1);
					FillMessage fm2 = new FillMessage(trd.getUser(), trd.getProduct(), 
							tPrice, t.getRemainingVolume(), "leaving " + 
									(trd.getRemainingVolume() - t.getRemainingVolume()), trd.getSide(), trd.getId());
					addFillMessage(fm2);
					trd.setRemainingVolume(trd.getRemainingVolume() - t.getRemainingVolume());
					t.setRemainingVolume(0);
					side.addOldEntry(t);
				}
				else{
					int remainder = t.getRemainingVolume() - trd.getRemainingVolume();
					Price tPrice;
					if(t.getPrice().isMarket()){
						tPrice = trd.getPrice();
					}
					else{
						tPrice = t.getPrice();
					}
					FillMessage fm1 = new FillMessage(t.getUser(), t.getProduct(), 
							tPrice, trd.getRemainingVolume(), "leaving " + remainder, t.getSide(), t.getId());
					addFillMessage(fm1);
					FillMessage fm2 = new FillMessage(trd.getUser(), trd.getProduct(), 
							tPrice, trd.getRemainingVolume(), "leaving 0", trd.getSide(), trd.getId());
					addFillMessage(fm2);
					t.setRemainingVolume(remainder);
					trd.setRemainingVolume(0);
					side.addOldEntry(trd);
				}
			}
		}
		for(Tradable t : tradedOut){
			entriesAtPrice.remove(t);
			if(entriesAtPrice.isEmpty()){
				side.clearIfEmpty(side.topOfBookPrice());
			}
		}
		return fillMessages;
	}

}
