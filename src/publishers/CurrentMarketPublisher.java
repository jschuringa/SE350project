package publishers;

import java.util.ArrayList;
import java.util.HashMap;
import client.User;

public final class CurrentMarketPublisher extends Publisher {
	
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
		HashMap<String, ArrayList<User>> subs = super.getSubscriptions();
		if(subs.containsKey(md.product)){
			for(User u : subs.get(md.product)){
				u.acceptCurrentMarket(md.product, md.buyPrice, md.buyVolume, md.sellPrice, md.sellVolume);
			}

		}
		
	}
	
	
	synchronized HashMap<String, ArrayList<User>> getSubscriptions(){
		HashMap<String, ArrayList<User>> temp = new HashMap<String, ArrayList<User>>(super.getSubscriptions());
		return temp;
	}

}
