package price;

public class Price {
	
	private final long value;
	
	Price(long value){
		this.value = value;
	}
	
	Price(){
		
	}
	
	public Price add(Price p){
		
	}
	
	public Price subtract(Price p){
		
	}
	
	public Price multiply(int p){
		
	}
	
	public int compareTo(Price p){
		
	}
	
	public boolean greaterOrEqual(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
	}
	
	public boolean greaterThan(Price p){
		if(this.isMarket() || p.isMarket())
		return false;
	}
	
	public boolean lessOrEqual(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
	}
	
	public boolean lessThan(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
	}
	
	public boolean equals(Price p){
		if(this.isMarket() || p.isMarket())
			return false;
	}
	
	public boolean isMarket(){
		
	}
	
	public boolean isNegative(){
		
	}
	
	public String toString(){
		if(isMarket())
			return "MKT";
		StringBuilder string = new StringBuilder("$");
		string.append(Long.toString(value));
		string.insert(string.length()-3, ".");
		return string.toString();
	}
	
	
}
