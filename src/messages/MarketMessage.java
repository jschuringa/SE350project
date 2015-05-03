package messages;

public class MarketMessage {
	private final MarketState state;
	
	MarketMessage(MarketState state){
		this.state = state;
	}
	
	public MarketState getMarketState(){
		return this.state;
	}
	
	public String toString(){
		return "Market: " + this.state.toString();
	}
}
