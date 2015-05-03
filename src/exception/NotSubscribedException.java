package exception;

public class NotSubscribedException extends Exception {
	public NotSubscribedException(String message) {
        super(message);
    }
	
	public NotSubscribedException() {
        super();
    }

}
