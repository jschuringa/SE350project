package book;

import java.util.ArrayList;
import java.util.HashMap;

import exception.DataValidationException;
import exception.InvalidBookOperation;
import exception.InvalidMarketStateException;
import exception.InvalidMarketStateTransition;
import exception.InvalidMessageException;
import exception.InvalidTradableOperation;
import exception.NoSuchProductException;
import exception.OrderNotFoundException;
import exception.ProductAlreadyExistsException;
import exception.UserNotConnectedException;
import messages.MarketMessage;
import messages.MarketState;
import publishers.MarketDataDTO;
import publishers.MessagePublisher;
import publishers.TickerPublisher;
import tradable.BookSide;
import tradable.Order;
import tradable.Quote;
import tradable.TradableDTO;

public final class ProductService {
	
	private HashMap<String, ProductBook> allBooks = new HashMap<String, ProductBook>();
	private MarketState currentMarketState = MarketState.CLOSED;
	private static volatile ProductService instance;
	
	private ProductService()
	{
		
	}
	
	public static ProductService getInstance(){
		if (instance == null)
			synchronized(ProductService.class){
				if (instance == null)
					instance = new ProductService();
			}
		return instance;
	}
	
	public synchronized ArrayList<TradableDTO> getOrdersWithRemainingQty(String userName, String product)
	{
		//if (allBooks.containsKey(product))
		//{
			ProductBook temp = allBooks.get(product);
			return temp.getOrdersWithRemainingQty(userName);
		//}
	}
	
	public synchronized MarketDataDTO getMarketData(String product)
	{
		ProductBook temp = allBooks.get(product);
		return temp.getMarketData();
	}
	
	public synchronized MarketState getMarketState()
	{
		return currentMarketState;
	}
	
	public synchronized String[][] getBookDepth(String product) throws NoSuchProductException
	{
		if (allBooks.containsKey(product))
		{
			ProductBook temp = allBooks.get(product);
			return temp.getBookDepth();
		}
		else
			throw new NoSuchProductException();
	}
	
	public synchronized ArrayList<String> getProductList()
	{
		return new ArrayList<String>(allBooks.keySet());
	}
	
	public synchronized void setMarketState(MarketState ms) throws InvalidMarketStateTransition, InvalidMessageException, InvalidTradableOperation, OrderNotFoundException, UserNotConnectedException
	{
		if (currentMarketState == MarketState.CLOSED && ms == MarketState.PREOPEN)
		{
			currentMarketState = ms;
		}
		else if (currentMarketState == MarketState.PREOPEN && ms == MarketState.OPEN)
		{
			currentMarketState = ms;
			for (ProductBook value : allBooks.values())
			{
				value.openMarket();
			}
		}
		else if (currentMarketState == MarketState.OPEN && ms == MarketState.CLOSED)
		{
			currentMarketState = ms;
			for (ProductBook value : allBooks.values())
			{
				value.closeMarket();
			}
		}
		else
		{
			throw new InvalidMarketStateTransition();
		}
		
		MarketMessage mm = new MarketMessage(ms);
		MessagePublisher mpInstance = MessagePublisher.getInstance();
		mpInstance.publishMarketMessage(mm);
	}
	
	public synchronized void createProduct(String product) throws DataValidationException, ProductAlreadyExistsException, InvalidBookOperation
	{
		if (product == null)
			throw new DataValidationException();
		
		if (allBooks.containsKey(product))
			throw new ProductAlreadyExistsException();
		
		ProductBook pb = new ProductBook(product);
		allBooks.put(product, pb);
	}
	
	public synchronized void submitQuote(Quote q) throws InvalidMarketStateException, NoSuchProductException, DataValidationException, InvalidTradableOperation, InvalidMessageException, UserNotConnectedException
	{
		if (currentMarketState == MarketState.CLOSED)
			throw new InvalidMarketStateException();
		
		if (allBooks.containsKey(q.getProduct()) == false)
			throw new NoSuchProductException();
		
		ProductBook temp = allBooks.get(q.getProduct());
		temp.addToBook(q);
	}
	
	public synchronized void submitOrder(Order o) throws InvalidMarketStateException, NoSuchProductException, InvalidMessageException, InvalidTradableOperation, UserNotConnectedException
	{
		if (currentMarketState == MarketState.CLOSED)
			throw new InvalidMarketStateException();
		
		if (currentMarketState == MarketState.PREOPEN && o.getPrice().isMarket() == true)
			throw new InvalidMarketStateException();
		
		if (allBooks.containsKey(o.getProduct()) == false)
			throw new NoSuchProductException();
		
		ProductBook temp = allBooks.get(o.getProduct());
		temp.addToBook(o);	
	}
	
	public synchronized void submitOrderCancel(String product, BookSide side, String orderID) throws InvalidMarketStateException, NoSuchProductException, OrderNotFoundException, InvalidMessageException
	{
		if (currentMarketState == MarketState.CLOSED)
			throw new InvalidMarketStateException();
		
		if (allBooks.containsKey(product) == false)
			throw new NoSuchProductException();
		
		ProductBook temp = allBooks.get(product);
		temp.cancelOrder(side,  orderID);
	}
	
	public synchronized void submitQuoteCancel(String userName, String product) throws InvalidMarketStateException, NoSuchProductException
	{
		if (currentMarketState == MarketState.CLOSED)
			throw new InvalidMarketStateException();
		
		if (allBooks.containsKey(product) == false)
			throw new NoSuchProductException();
		
		ProductBook temp = allBooks.get(product);
		temp.cancelQuote(userName);
	}

}
