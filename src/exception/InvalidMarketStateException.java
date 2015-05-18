package exception;

public class InvalidMarketStateException extends Exception {
	public InvalidMarketStateException(String message) {
        super(message);
    }
	
	public InvalidMarketStateException() {
        super();
    }
}