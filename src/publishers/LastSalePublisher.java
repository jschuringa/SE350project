package publishers;

import java.util.ArrayList;
import java.util.HashMap;

import client.User;
import price.Price;
import price.PriceFactory;



public final class LastSalePublisher extends Publisher {
	private volatile static LastSalePublisher instance;
	
	
	private LastSalePublisher(){}
	
	public static LastSalePublisher getInstance(){
		if (instance == null)
			synchronized(LastSalePublisher.class){
				if (instance == null)
					instance = new LastSalePublisher();
			}
		return instance;
	}
	
	
	public synchronized void publishLastSale(String product, Price p, int v){
		HashMap<String, ArrayList<User>> subs = super.getSubscriptions();
		if(subs.containsKey(product)){
			for(User u : subs.get(product)){
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
	
	synchronized HashMap<String, ArrayList<User>> getSubscriptions(){
		HashMap<String, ArrayList<User>> temp = new HashMap<String, ArrayList<User>>(super.getSubscriptions());
		return temp;
	}
	
}
