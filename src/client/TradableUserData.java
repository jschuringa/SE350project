package client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.DataValidationException;
import exception.InvalidTradableOperation;
import exception.OrderNotFoundException;
import tradable.BookSide;

public class TradableUserData {
	
	private String userName;
	private String stockSymbol;
	private String orderID;
	private BookSide side;
	
	public TradableUserData(String userName, String stockSymbol, String orderID, BookSide side) throws InvalidTradableOperation, OrderNotFoundException
	{
		this.userName = setUserName(userName);
		this.stockSymbol = setProduct(stockSymbol);
		this.orderID = setOrderId(orderID);
		this.side = setBookSide(side);
	}
	
	public String getUserName()
	{
		return this.userName;
	}
	
	public String getProduct()
	{
		return this.stockSymbol;
	}
	
	public String getOrderId()
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
	
	private String setProduct(String stockSymbol) throws InvalidTradableOperation
	{
		Pattern p = Pattern.compile("[a-zA-Z]{1,4}");
		Matcher m = p.matcher(stockSymbol);
		if(stockSymbol == null || !m.matches() ){
			throw new InvalidTradableOperation(stockSymbol + " is invalid");
		}
		else
		{
			return stockSymbol.toUpperCase();
		}
	}
	
	private String setOrderId(String orderID) throws InvalidTradableOperation, OrderNotFoundException
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
	
	private BookSide setBookSide(BookSide side) throws InvalidTradableOperation
	{
		if(side != null)
		{
			return side;
		}
		else{
			throw new InvalidTradableOperation("Side cannot be null");
		}
	}
	
	public String toString()
	{
		return "User " + this.userName + ", " + this.side.toString() + " " + this.stockSymbol + " (" + this.orderID + ")";
		
	}

}
