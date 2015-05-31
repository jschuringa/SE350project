package exception;

public class AlreadyConnectedException extends Exception {
	public AlreadyConnectedException(String message) {
        super(message);
    }
	
	public AlreadyConnectedException() {
        super();
    }
}
