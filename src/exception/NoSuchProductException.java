package exception;

public class NoSuchProductException extends Exception {
	public NoSuchProductException(String message) {
        super(message);
    }
	
	public NoSuchProductException() {
        super();
    }
}
