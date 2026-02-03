package Adventure_generator.Exception;
/**
 * Exception thrown when a requested resource is not found.
 * Used for returning 404 HTTP responses.
 * 
 * @author Astra K. Nguyen
 * @version 1.0.0
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message); // Calls RunTimeException parent 
    }
    public NotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}