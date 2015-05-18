package exception;

public class InvalidBookOperation extends Exception {
	public InvalidBookOperation(String message) {
        super(message);
    }
	
	public InvalidBookOperation() {
        super();
    }
}
