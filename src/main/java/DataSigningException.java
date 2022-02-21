public class DataSigningException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataSigningException(String message) {
        super(message);
    }

    public DataSigningException(String message, Throwable cause) {
        super(message, cause);
    }

}