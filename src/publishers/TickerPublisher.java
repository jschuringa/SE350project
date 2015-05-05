package publishers;

import java.util.ArrayList;
import java.util.HashMap;

import client.User;
import price.Price;
import price.PriceFactory;

public final class TickerPublisher extends Publisher {
	private volatile HashMap<String, Price> tickermap = new HashMap<String, Price>();
	private static TickerPublisher instance;

	private TickerPublisher(){}

	public static TickerPublisher getInstance(){
		if (instance == null)
			synchronized(TickerPublisher.class){
				if (instance == null)
					instance = new TickerPublisher();
			}
		return instance;
	}

	public synchronized void publishTicker(String product, Price p){
		if(p == null){
			p = PriceFactory.makeLimitPrice(0);
		}
		HashMap<String, ArrayList<User>> subs = super.getSubscriptions();
		if(subs.containsKey(product)){
			if(tickermap.containsKey(product)){
				if(tickermap.containsKey(product)){
					Price oldvalue = tickermap.get(product);
					for(User u : subs.get(product)){
						if(oldvalue.equals(p)){
							u.acceptTicker(product, p, '=');
						}
						else if(oldvalue.greaterThan(p)){
							u.acceptTicker(product, p, (char)8595);
						}
						else{
							u.acceptTicker(product, p, (char) 8593);
						}
					}
				}
			} else {
				for(User u : subs.get(product)){
					u.acceptTicker(product, p, ' ');
				}
			}
		}
		tickermap.put(product, p);
	}
	
	synchronized HashMap<String, ArrayList<User>> getSubscriptions(){
		HashMap<String, ArrayList<User>> temp = new HashMap<String, ArrayList<User>>(super.getSubscriptions());
		return temp;
	}
}
