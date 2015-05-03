package exception;

public class InvalidMessageException extends Exception {
	public InvalidMessageException(String message) {
        super(message);
    }
	
	public InvalidMessageException() {
        super();
    }

}
