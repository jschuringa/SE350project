package exception;

public class InvalidConnectionIdException extends Exception {
	public InvalidConnectionIdException(String message) {
        super(message);
    }
	
	public InvalidConnectionIdException() {
        super();
    }

}