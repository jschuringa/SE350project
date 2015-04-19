package price;

import exception.InvalidPriceOperation;

public class Price {
	
	private final long value;
	private final boolean market;
	
	Price(long value){
		this.value = value;
		this.market = false;
	}
	
	Price(){
		this.value = 0;
		this.market = true;
	}
	
	public long getValue() throws InvalidPriceOperation {
		if(this.isMarket())
			throw new InvalidPriceOperation();
		else
			return this.value;
	}
	public Price add(Price p) throws InvalidPriceOperation {
		if(this.isMarket() || p.isMarket())
			throw new InvalidPriceOperation();
		return PriceFactory.makeLimitPrice(this.value + p.getValue()); 
	}
	
	public Price subtract(Price p) throws InvalidPriceOperation {
		if(this.isMarket() || p.isMarket())
			throw new InvalidPriceOperation();
		return PriceFactory.makeLimitPrice(this.value - p.getValue());
	}
	
	public Price multiply(int p) throws InvalidPriceOperation {
		if(this.isMarket())
			throw new InvalidPriceOperation();
		return PriceFactory.makeLimitPrice(this.value * p);
	}
	
	public int compareTo(Price p){
		if(this.isMarket() || p.isMarket())
			return -1;
		else if(this.equals(p))
			return 0;
		else if(this.greaterThan(p))
			return 1;
		else
			return -1;
	}
	
	public boolean greaterOrEqual(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
		else
			return this.value >= p.value;
	}
	
	public boolean greaterThan(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
		else
			return this.value > p.value;
	}
	
	public boolean lessOrEqual(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
		else
			return this.value <= p.value;
	}
	
	public boolean lessThan(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
		else
			return this.value < p.value;
	}
	
	public boolean equals(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
		else
			return this.value == p.value;
	}
	
	public boolean isMarket(){
		return this.market;
	}
	
	public boolean isNegative(){
		return this.value < 0;
	}
	
	public String toString(){
		if(isMarket())
			return "MKT";
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
