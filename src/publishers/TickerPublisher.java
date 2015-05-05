package publishers;

import java.util.ArrayList;
import java.util.HashMap;

import client.User;
import price.Price;
import price.PriceFactory;

public class TickerPublisher extends Publisher {
	private volatile HashMap<String, ArrayList<User>> subscriptions = super.getSubscriptions();
	private volatile static HashMap<String, Price> tickermap = new HashMap<String, Price>();
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

	public synchronized static void publishTicker(String product, Price p){
		if(p == null){
			p = PriceFactory.makeLimitPrice(0);
		}
		if(subscriptions.contains(product)){
			for(User u : subscriptions.get(product)){
				if(tickermap.containsKey(product)){
					Price oldvalue = tickermap.get(product);
					if(oldvalue.equals(p)){
						u.acceptTicker(product, p, '=');
						break;
					}
					if(oldvalue.greaterThan(p)){
						u.acceptTicker(product, p, (char)8595);
						break;
					}
					if(oldvalue.lessThan(p)){
						u.acceptTicker(product, p, (char) 8593);
					}
				} else {
					u.acceptTicker(product, p, ' ');
					tickermap.put(product, p);
				}
			}
		}
	}
}
