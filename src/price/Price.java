package price;

import exception.InvalidPriceOperation;

public class Price implements Comparable<Price>{
	Price(){}
	
	public long getValue() throws InvalidPriceOperation {
		throw new InvalidPriceOperation("Invalid method on Market Price: getValue()");
	}
	
	public Price add(Price p) throws InvalidPriceOperation {
		throw new InvalidPriceOperation("Invalid method on Market Price: add()");
	}
	
	public Price subtract(Price p) throws InvalidPriceOperation {
		throw new InvalidPriceOperation("Invalid method on Market Price: subtract()");
	}
	
	public Price multiply(int p) throws InvalidPriceOperation {
		throw new InvalidPriceOperation("Invalid method on Market Price: multiply()");
	}
	
	@Override
	public int compareTo(Price p){
		return -1;
	}
	
	public boolean greaterOrEqual(Price p){
		return false;
	}
	
	public boolean greaterThan(Price p){
		return false;
	}
	
	public boolean lessOrEqual(Price p){
		return false;
	}
	
	public boolean lessThan(Price p){
		return false;
	}
	
	public boolean equals(Price p){
		return false;
	}
	
	public boolean isMarket(){
		return true;
	}
	
	public boolean isNegative(){
		return false;
	}
	
	public String toString(){
		return "MKT";
		
	}
}
	