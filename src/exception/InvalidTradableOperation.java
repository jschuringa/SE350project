package exception;

public class InvalidTradableOperation extends Exception {
	public InvalidTradableOperation(String message) {
        super(message);
    }
	
	public InvalidTradableOperation() {
        super();
    }
}
