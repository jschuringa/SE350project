package publishers;

import java.util.ArrayList;
import java.util.HashMap;
import client.User;

public final class CurrentMarketPublisher extends Publisher {
	
	private volatile HashMap<String, ArrayList<User>> subscriptions = super.getSubscriptions();
	private static CurrentMarketPublisher instance;
	
	private CurrentMarketPublisher(){}
	
	public static CurrentMarketPublisher getInstance(){
		if(instance == null)
			synchronized(CurrentMarketPublisher.class){
				if(instance == null)
					instance = new CurrentMarketPublisher();
			}
		return instance;
	}
	
	
	
	
	public synchronized void publishCurrentMarket(MarketDataDTO md)
	{
		if(subscriptions.containsKey(md.product)){
			for(User u : subscriptions.get(md.product)){
				u.acceptCurrentMarket(md.product, md.buyPrice, md.buyVolume, md.sellPrice, md.sellVolume);
			}

		}
		
	}
	
	
	synchronized HashMap<String, ArrayList<User>> getSubscriptions(){
		HashMap<String, ArrayList<User>> temp = new HashMap<String, ArrayList<User>>(subscriptions);
		return temp;
	}

}
