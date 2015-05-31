package exception;

public class UserNotConnectedException extends Exception {
	public UserNotConnectedException(String message) {
        super(message);
    }
	
	public UserNotConnectedException() {
        super();
    }

}
