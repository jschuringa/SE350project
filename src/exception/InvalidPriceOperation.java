package exception;

public class InvalidPriceOperation extends Exception {
	public InvalidPriceOperation(String message) {
        super(message);
    }
	
	public InvalidPriceOperation() {
        super();
    }
}
