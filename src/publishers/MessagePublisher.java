package publishers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import messages.CancelMessage;
import messages.FillMessage;
import messages.MarketMessage;
import client.User;

public class MessagePublisher extends Publisher {
	private volatile HashMap<String, ArrayList<User>> subscriptions = super.getSubscriptions();
	private static MessagePublisher instance;
	
	private MessagePublisher(){}
	
	public static MessagePublisher getInstance(){
		if(instance == null)
			synchronized(MessagePublisher.class){
				if(instance == null)
					instance = new MessagePublisher();
			}
		return instance;
	}
	
	
	public synchronized void publishCancel(CancelMessage cm){
		if(this.subscriptions.containsKey(cm.getProduct())){
			for(User u : subscriptions.get(cm.getProduct())){
				if(u.getUserName().equals(cm.getUser())){
					u.acceptMessage(cm);
					return;
				}
			}
		}
	}
	
	public synchronized void publishFill(FillMessage fm) {
		if(subscriptions.containsKey(fm.getProduct())){
			for(User u : subscriptions.get(fm.getProduct())){
				if(u.getUserName().equals(fm.getUser())){
					u.acceptMessage(fm);
					break;
				}
			}
		}
	}
	
	public synchronized void publishMarketMessage(MarketMessage mm) {
		Collection<ArrayList<User>> users = subscriptions.values();
		HashSet<User> usersSeen = new HashSet<User>();
		if(!users.isEmpty()){
			for(ArrayList<User> list : users){
				if(!list.isEmpty()){
					for(User u : list){
						if(!usersSeen.contains(u)){
							usersSeen.add(u);
							u.acceptMarketMessage(mm.toString());
						}
					}
				}
			}
		}
	}
	
	synchronized HashMap<String, ArrayList<User>> getSubscriptions(){
		HashMap<String, ArrayList<User>> temp = new HashMap<String, ArrayList<User>>(subscriptions);
		return temp;
	}

}
