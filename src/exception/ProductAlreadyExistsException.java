package exception;

public class ProductAlreadyExistsException extends Exception {
	public ProductAlreadyExistsException(String message) {
        super(message);
    }
	
	public ProductAlreadyExistsException() {
        super();
    }

}
