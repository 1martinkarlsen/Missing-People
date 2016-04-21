package exception;

public class UnknownServerException extends Exception {
    
    public UnknownServerException() {
    }
    
    public UnknownServerException(String msg) {
        super(msg);
    }
}
