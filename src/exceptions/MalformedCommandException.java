package exceptions;

public class MalformedCommandException extends Exception {
    public MalformedCommandException() {

    }
    public MalformedCommandException(String message) {
        super (message);
    }

    public MalformedCommandException(Throwable cause) {
        super (cause);
    }

    public MalformedCommandException(String message, Throwable cause) {
        super (message, cause);
    }
}
