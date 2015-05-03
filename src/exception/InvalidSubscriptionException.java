package exception;

public class InvalidSubscriptionException extends Exception {
	public InvalidSubscriptionException(String message) {
        super(message);
    }
	
	public InvalidSubscriptionException() {
        super();
    }

}
