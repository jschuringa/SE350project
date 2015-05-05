package publishers;

import java.util.ArrayList;
import java.util.HashMap;

import client.User;
import price.Price;
import price.PriceFactory;



public final class LastSalePublisher extends Publisher {
	private volatile HashMap<String, ArrayList<User>> subscriptions = super.getSubscriptions();
	private static LastSalePublisher instance;
	
	
	private LastSalePublisher(){}
	
	public static LastSalePublisher getLastSaleInstance(){
		if (instance == null)
			synchronized(LastSalePublisher.class){
				if (instance == null)
					instance = new LastSalePublisher();
			}
		return instance;
	}
	
	
	public synchronized void publishLastSale(String product, Price p, int v){
		if(subscriptions.containsKey(product)){
			for(User u : subscriptions.get(product)){
				if(p == null){
					u.acceptLastSale(product, PriceFactory.makeLimitPrice(0), v);
				} else {
					u.acceptLastSale(product, p, v);
				}
			}
			TickerPublisher tmp = TickerPublisher.getInstance();
			tmp.publishTicker(product, p);
		}
	}
	
}
