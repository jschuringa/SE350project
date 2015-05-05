package publishers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import messages.CancelMessage;
import messages.FillMessage;
import messages.MarketMessage;
import client.User;

public final class MessagePublisher extends Publisher {
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
		HashMap<String, ArrayList<User>> subs = super.getSubscriptions();
		if(subs.containsKey(cm.getProduct())){
			for(User u : subs.get(cm.getProduct())){
				if(u.getUserName().equals(cm.getUser())){
					u.acceptMessage(cm);
					return;
				}
			}
		}
	}
	
	public synchronized void publishFill(FillMessage fm) {
		HashMap<String, ArrayList<User>> subs =  super.getSubscriptions();
		if(subs.containsKey(fm.getProduct())){
			for(User u : subs.get(fm.getProduct())){
				if(u.getUserName().equals(fm.getUser())){
					u.acceptMessage(fm);
					break;
				}
			}
		}
	}
	
	public synchronized void publishMarketMessage(MarketMessage mm) {
		HashMap<String, ArrayList<User>> subs = super.getSubscriptions();
		Collection<ArrayList<User>> users = subs.values();
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
		HashMap<String, ArrayList<User>> temp = new HashMap<String, ArrayList<User>>(super.getSubscriptions());
		return temp;
	}

}
