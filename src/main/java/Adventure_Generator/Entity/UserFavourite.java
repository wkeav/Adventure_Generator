package Adventure_generator.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * UserFavourite Entity - Join table for User-Adventure favorites relationship
 * 
 * Represents a many-to-many relationship between users and adventures through
 * a join table. This allows users to mark specific adventures as favorites
 * while maintaining referential integrity and preventing duplicate entries.
 * 
 * Database Mapping:
 * - Table: user_favourites
 * - Primary Key: id (auto-generated)
 * - Foreign Keys: user_id, adventure_id (both non-nullable)
 * - Unique Constraint: (user_id, adventure_id) prevents duplicate favorites per user
 * - Indexes: B-tree indexes on user_id and adventure_id for optimized queries
 * 
 * Relationships:
 * - User: Many favorites belong to one user (ManyToOne with LAZY fetch)
 * - Adventure: Many favorites reference one adventure (ManyToOne with LAZY fetch)
 * 
 * Lifecycle:
 * - createdAt: Automatically set on persistence via @PrePersist callback
 * - Immutable: createdAt is not updatable after creation
 * 
 * @see User
 * @see Adventure
 */
@Entity
@Table(
    name = "user_favourites",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "adventure_id"}),
    indexes = { // B-tree indexes for efficient lookups on foreign keys
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_adventure_id", columnList = "adventure_id")
    }
)
public class UserFavourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    /** Foreign key reference to User entity. LAZY loaded to optimize performance. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    /** Foreign key reference to Adventure entity. LAZY loaded to optimize performance. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="adventure_id", nullable=false)
    private Adventure adventure;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt; 

    // CONSTRUCTOR 
    public UserFavourite(){}

    /**
     * Convenience constructor to create a favorite relationship between a user and adventure.
     * 
     * @param user The user marking this adventure as favorite (non-null)
     * @param adventure The adventure being marked as favorite (non-null)
     */
    public UserFavourite(User user, Adventure adventure){
        this.user = user;
        this.adventure = adventure; 
    }

    // Getters & Setters
    public Long getId(){
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Adventure getAdventure() {
        return adventure;
    }

    public void setAdventure(Adventure adventure) {
        this.adventure = adventure;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // JPA Lifecycle Callbacks
    /**
     * JPA lifecycle callback invoked automatically before the entity is persisted to the database.
     * Sets the creation timestamp to the current time.
     * 
     * This ensures every favorite record has a precise creation timestamp without requiring
     * explicit setter calls from the application layer.
     */
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    // Object Methods
    /**
     * String representation of this UserFavourite for logging and debugging purposes.
     * 
     * @return A formatted string containing all entity fields
     */
    @Override
    public String toString() {
        return "UserFavourite [id=" + id + ", user=" + user + ", adventure=" + adventure + ", createdAt=" + createdAt
                + "]";
    }

    
}
