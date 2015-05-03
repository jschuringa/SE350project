package publishers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.User;
import exception.AlreadySubscribedException;
import exception.InvalidSubscriptionException;
import exception.NotSubscribedException;

public abstract class Publisher {
	private volatile HashMap<String, ArrayList<User>> subscriptions = new HashMap<String, ArrayList<User>>();
	
	public synchronized void subscribe(User u, String product) throws AlreadySubscribedException, InvalidSubscriptionException{
		if(u == null)
			throw new InvalidSubscriptionException("User cannot be null");
		if(product == null)
			throw new InvalidSubscriptionException("Product cannot be null");
		Pattern p = Pattern.compile("[a-zA-Z]{1,4}");
		Matcher m = p.matcher(product);
		if(m.matches()){
			if(subscriptions.containsKey(product)){
				if(subscriptions.get(product).contains(u))
					throw new AlreadySubscribedException(u + " is already subscribed to " + product);
				subscriptions.get(product).add(u);
			}
			else{
				ArrayList<User> temp = new ArrayList<User>();
				temp.add(u);
				subscriptions.put(product, temp);
			}
		}
		else
			throw new InvalidSubscriptionException(product + " is invalid product");
	}
	
	public synchronized void unSubscribe(User u, String product) throws NotSubscribedException, InvalidSubscriptionException{
		if(u == null)
			throw new InvalidSubscriptionException("User cannot be null");
		if(product == null)
			throw new InvalidSubscriptionException("Product cannot be null");
		Pattern p = Pattern.compile("[a-zA-Z]{1,4}");
		Matcher m = p.matcher(product);
		if(m.matches()){
			if(subscriptions.containsKey(product)){
				if(!subscriptions.get(product).contains(u))
					throw new NotSubscribedException(u + " is not subscribed to " + product);
				subscriptions.get(product).remove(u);
			}
			else{
				throw new NotSubscribedException(u + " is not subscribed to " + product);
			}
		}
		else
			throw new InvalidSubscriptionException(product + " is invalid product");
	}
	
	synchronized HashMap<String, ArrayList<User>> getSubscriptions(){
		return subscriptions;
	}
}
