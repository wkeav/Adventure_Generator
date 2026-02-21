package Adventure_generator.Exception;

/**
 * Exception thrown when a requested resource is not found in the database.
 * 
 * Used primarily for returning 404 HTTP responses when:
 * - User requests a non-existent adventure
 * - Adventure or User entity lookup fails
 * - Favorite relationship doesn't exist
 * 
 * This is a RuntimeException, so it doesn't require explicit throws declarations.
 */
public class NotFoundException extends RuntimeException {
    
    /**
     * Constructs a new NotFoundException with the specified detail message.
     * @param message The detail message explaining what was not found
     */
    public NotFoundException(String message){
        super(message);
    }
    
    /**
     * Constructs a new NotFoundException with the specified detail message and cause.
     * @param message The detail message explaining what was not found
     * @param cause The underlying cause of the exception
     */
    public NotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}