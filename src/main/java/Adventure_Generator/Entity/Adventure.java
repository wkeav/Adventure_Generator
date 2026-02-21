package Adventure_generator.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Adventure Entity - Generated adventure recommendations for users
 * 
 * Represents a generated adventure recommendation for a user based on mood, weather,
 * and distance preferences. Each adventure record is immutable after creation and
 * tracks when it was created and last modified.
 * 
 * Database Mapping:
 * - Table: adventure
 * - Primary Key: id (auto-generated)
 * - Foreign Key: user_id (non-nullable) - Links to the user who generated this adventure
 * 
 * Relationships:
 * - User: Many adventures belong to one user (ManyToOne with LAZY fetch)
 * - UserFavourite: One-to-many relationship via join table for favorites tracking
 * 
 * Features:
 * - Automatic timestamp management (createdAt immutable, updatedAt on changes)
 * - Lazy loading of user relationship for performance optimization
 * - Adventure preference tracking (mood, weather, distance)
 * - Equals and hashCode based on persisted ID for Set/Map usage
 * 
 * Lifecycle:
 * - createdAt: Set once on initial persistence, never updated
 * - updatedAt: Updated on entity modification via @PreUpdate
 * 
 * @see User
 * @see UserFavourite
 */
@Entity
@Table(name = "adventure")
public class Adventure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Adventure is required")
    @Column(columnDefinition="TEXT", nullable=false)
    private String adventure;
    
    /** Foreign key reference to User entity. LAZY loaded to optimize performance. Non-nullable. */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user; 

    @NotBlank(message="Mood is required")
    @Column(nullable=false, length=20)
    private String mood;

    @NotBlank(message="Weather is required")
    @Column(nullable=false, length=50)
    private String weather;

    @Column(name="is_long_distance")
    private Boolean isLongDistance = false; 

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;


    // CONSTRUCTOR
    public Adventure(){}

    /**
     * Constructor to create an adventure with all required fields.
     * 
     * @param adventure The adventure description/recommendation text (non-null)
     * @param user The user who generated/owns this adventure (non-null)
     * @param mood The mood preference that generated this adventure (non-null)
     * @param weather The weather condition preference (non-null)
     * @param isLongDistance Whether this adventure involves long-distance travel (null-safe, defaults to false)
     */
    public Adventure(String adventure, User user, String mood, String weather, Boolean isLongDistance) {
        this.adventure = adventure;
        this.user = user;
        this.mood = mood;
        this.weather = weather;
        this.isLongDistance = isLongDistance != null ? isLongDistance : false;
    }

    // JPA Lifecycle Callbacks
    /**
     * JPA lifecycle callback invoked automatically before the entity is persisted to the database.
     * Initializes both createdAt and updatedAt timestamps to the current time.
     * 
     * This ensures every adventure record has accurate creation and initial update timestamps
     * without requiring explicit setter calls from the application layer.
     */
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * JPA lifecycle callback invoked automatically before the entity is updated in the database.
     * Updates the updatedAt timestamp to the current time.
     * 
     * This maintains an accurate record of when the adventure was last modified.
     */
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdventure() {
        return adventure;
    }

    public void setAdventure(String adventure) {
        this.adventure = adventure;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Boolean getIsLongDistance() {
        return isLongDistance;
    }

    /**
     * Sets the long-distance preference with null-safe default behavior.
     * @param isLongDistance True for long-distance, false otherwise, null treated as false
     */
    public void setIsLongDistance(Boolean isLongDistance) {
        this.isLongDistance = isLongDistance != null ? isLongDistance : false;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    // Object Methods
    /**
     * String representation of this Adventure for logging and debugging purposes.
     * 
     * @return A formatted string containing all adventure fields
     */
    @Override
    public String toString() {
        return "Adventure{" +
                "id=" + id +
                ", adventure='" + adventure + '\'' +
                ", mood='" + mood + '\'' +
                ", weather='" + weather + '\'' +
                ", isLongDistance=" + isLongDistance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * Compares this adventure with another object for equality.
     * 
     * For persisted entities (with non-null IDs), equality is based on ID only.
     * For unpersisted entities (IDs are null), equality compares all fields:
     * adventure, mood, weather, and isLongDistance.
     * 
     * This approach supports both new and persisted entities in collections and lookups.
     * 
     * @param o The object to compare with
     * @return true if the adventures are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adventure adventure1 = (Adventure) o;

        // Compare by ID only if both are persisted (have non-null IDs)
        if (id != null && adventure1.id != null) {
            return id.equals(adventure1.id);
        }

        // For unpersisted entities, compare all fields
        if (!adventure.equals(adventure1.adventure)) return false;
        if (!mood.equals(adventure1.mood)) return false;
        if (!weather.equals(adventure1.weather)) return false;
        return isLongDistance.equals(adventure1.isLongDistance);
    }

    /**
     * Generates a hash code for this adventure.
     * 
     * For persisted entities (non-null ID), uses the ID hash.
     * For unpersisted entities, combines hashes of all fields:
     * adventure, mood, weather, and isLongDistance.
     * 
     * Consistent with equals() contract to support proper Set and Map behavior.
     * 
     * @return The hash code for this adventure
     */
    @Override
    public int hashCode() {
        // Use ID if available, otherwise use fields
        if (id != null) {
            return id.hashCode();
        }
        int result = adventure.hashCode();
        result = 31 * result + mood.hashCode();
        result = 31 * result + weather.hashCode();
        result = 31 * result + isLongDistance.hashCode();
        return result;
    }

}
