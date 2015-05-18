package price;

import exception.InvalidPriceOperation;

public class LimitPrice extends Price implements Comparable<Price>{
	private final long value;
	
	LimitPrice(long value){
		this.value = value;
	}
	
	public long getValue() throws InvalidPriceOperation {
		return this.value;
	}
	
	public Price add(Price p) throws InvalidPriceOperation {
		return PriceFactory.makeLimitPrice(this.value + p.getValue()); 
	}
	
	public Price subtract(Price p) throws InvalidPriceOperation {
		return PriceFactory.makeLimitPrice(this.value - p.getValue());
	}
	
	public Price multiply(int p) throws InvalidPriceOperation {
		return PriceFactory.makeLimitPrice(this.value * p);
	}
	
	@Override
	public int compareTo(Price p){
		if(this.equals(p))
			return 0;
		else if(this.greaterThan(p))
			return 1;
		else
			return -1;
	}
	
	public boolean greaterOrEqual(Price p){
		try {
			return this.value >= p.getValue();
		}
		catch (InvalidPriceOperation e) {
			return false;
		}
	}
	
	public boolean greaterThan(Price p){
		try {
			return this.value > p.getValue();
		}
		catch (InvalidPriceOperation e) {
			return false;
		}
	}
	
	public boolean lessOrEqual(Price p){
		try {
			return this.value <= p.getValue();
		}
		catch (InvalidPriceOperation e) {
			return false;
		}
	}
	
	public boolean lessThan(Price p){
		try {
			return this.value < p.getValue();
		}
		catch (InvalidPriceOperation e) {
			return false;
		}
	}
	
	public boolean equals(Price p){
		try {
			return this.value == p.getValue();
		}
		catch (InvalidPriceOperation e) {
			return false;
		}
	}
	
	public boolean isMarket(){
		return false;
	}
	
	public boolean isNegative(){
		return this.value < 0;
	}

	public String toString(){
		StringBuilder string = new StringBuilder("$");
		string.append(value);
		int len = string.length();
		if(this.isNegative()){
			if(len == 3)
				string.insert(2,"0.0");
			else if(len == 4)
				string.insert(2,"0.");
			else{
				int decimal = len-2;
				string.insert(decimal, ".");
				for(int i = decimal - 3; i > 2; i = i-3){
					string.insert(i, ",");
				}
			}
		}
		else{
			if(len == 2)
				string.insert(1,"0.0");
			else if(len == 3)
				string.insert(1,"0.");
			else{
				int decimal = len-2;
				string.insert(decimal, ".");
				for(int i = decimal - 3; i > 1; i = i-3){
					string.insert(i, ",");
				}
			}
		}
		return string.toString();
	}
}
