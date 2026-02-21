package Adventure_generator.Entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * User Entity - Application user accounts
 * 
 * Represents a registered user in the adventure generator application.
 * Stores authentication credentials and user metadata.
 * 
 * Database Mapping:
 * - Table: users
 * - Primary Key: id (auto-generated)
 * - Unique Constraints: email, userName
 * 
 * Relationships:
 * - Adventure: One-to-many relationship (one user has many adventures)
 * - UserFavourite: One-to-many relationship via join table for favorites
 * 
 * Security:
 * - password field stores hashed passwords (BCrypt)
 * - email and userName must be unique
 * 
 * Lifecycle:
 * - createdAt: Automatically set on persistence via @PrePersist callback
 * 
 * @see Adventure
 * @see UserFavourite
 */
@Entity
@Table(name = "\"users\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String email;
    
    @Column(nullable=false, unique=true)
    private String userName;
    
    /** Hashed password using BCrypt. Never store plain text passwords. */
    @Column(nullable=false)
    private String password;

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;
    
    // CONSTRUCTOR
    public User(){
    }
    
    /**
     * Constructor to create a user with all fields.
     * 
     * @param id The user ID
     * @param email The user's email address (must be unique)
     * @param userName The user's display name (must be unique)
     * @param password The hashed password
     * @param createdAt The account creation timestamp
     */
    public User(long id, String email, String userName, String password, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.createdAt = createdAt;
    }
    
    // JPA Lifecycle Callbacks
    /**
     * JPA lifecycle callback invoked automatically before the entity is persisted to the database.
     * Sets the creation timestamp to the current time.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters & Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Object Methods
    /**
     * String representation of this User for logging and debugging purposes.
     * WARNING: Password hash is included - use with caution in production logs.
     * 
     * @return A formatted string containing all user fields
     */
    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", userName=" + userName + ", password=[PROTECTED]"
            + ", createdAt=" + createdAt + "]";
    }



}
