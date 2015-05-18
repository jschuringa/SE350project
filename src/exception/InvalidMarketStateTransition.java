package exception;

public class InvalidMarketStateTransition extends Exception {
	public InvalidMarketStateTransition(String message) {
        super(message);
    }
	
	public InvalidMarketStateTransition() {
        super();
    }
}
