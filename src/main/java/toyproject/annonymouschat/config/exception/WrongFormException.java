package toyproject.annonymouschat.config.exception;

public class WrongFormException extends Exception {
    public WrongFormException() {
        super();
    }

    public WrongFormException(String message) {
        super(message);
    }

    public WrongFormException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongFormException(Throwable cause) {
        super(cause);
    }
}
