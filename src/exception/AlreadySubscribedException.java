package exception;

public class AlreadySubscribedException extends Exception {
	public AlreadySubscribedException(String message) {
        super(message);
    }
	
	public AlreadySubscribedException() {
        super();
    }
}
