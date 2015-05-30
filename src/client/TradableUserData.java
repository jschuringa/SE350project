package client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidTradableOperation;
import exception.OrderNotFoundException;
import tradable.BookSide;

public class TradableUserData {
	
	private String userName;
	private String stockSymbol;
	private String orderID;
	private BookSide side;
	
	public TradableUserData(String userName, String stockSymbol, String orderID, BookSide side)
	{
		
	}
	
	public String getUserName()
	{
		return this.userName;
	}
	
	public String getStockSymbol()
	{
		return this.stockSymbol;
	}
	
	public String getOrderID()
	{
		return this.orderID;
	}
	
	public BookSide getSide()
	{
		return this.side;
	}
	
	private String setUserName(String userName) throws InvalidTradableOperation
	{
		Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
		Matcher m = p.matcher(userName);
		if(userName != null && m.matches() ){
			return userName;
		}
		else{
			throw new InvalidTradableOperation(userName + " is invalid");
		}
	}
	
	private String setStockSymbol(String stockSymbol) throws InvalidTradableOperation
	{
		if(stockSymbol != null)
		{
			return stockSymbol;
		}
		else
		{
			throw new InvalidTradableOperation(stockSymbol + " is invalid");
		}
	}
	
	private String setOrderID(String orderID) throws InvalidTradableOperation, OrderNotFoundException
	{
		if(orderID != null)
		{
			return orderID;
		}
		else
		{
			throw new OrderNotFoundException(orderID + " is invalid");
		}
	}
	
	private void setBookSide(BookSide side)
	{
		if(side != null)
		{
			
		}
	}

}
