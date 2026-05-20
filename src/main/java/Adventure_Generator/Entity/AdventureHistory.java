package Adventure_generator.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a user's adventure history record.
 * 
 * Tracks when a user completes, starts, or marks an adventure as done.
 * Maintains a relationship with both User and Adventure entities.
 * 
 * Database Table: adventure_history
 * 
 * @see User
 * @see Adventure
 * 
 * @author Adventure Generator Team
 * @version 1.0
 */
@Entity
@Table(name = "abdventure_history", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_adventure_id", columnList = "adventure_id")
})
public class AdventureHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Foreign key reference to User. LAZY loaded to optimize performance. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Foreign key reference to Adventure. LAZY loaded to optimize performance. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adventure_id", nullable = false)
    private Adventure adventure;

    /**
     * Status of the adventure completion.
     * Possible values: "started", "completed", "abandoned"
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status = "started"; // Default status when adventure is started

    /**
     * Optional notes/comments about the adventure experience.
     * Allows users to add feedback or memories.
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Rating given by user (1-5 stars).
     * Null if adventure not yet rated.
     */
    @Column(name = "rating")
    private Integer rating;

    /**
     * Timestamp when the adventure was first started.
     * Set automatically on entity creation.
     */
    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    /**
     * Timestamp when the adventure was completed.
     * Null until user marks adventure as completed.
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * Timestamp when this record was created in the database.
     * Set automatically on entity creation.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // CONSTRUCTORS
    public AdventureHistory() {}

    /**
     * Constructor to create an adventure history record.
     * 
     * @param user The user completing the adventure (non-null)
     * @param adventure The adventure being tracked (non-null)
     */
    public AdventureHistory(User user, Adventure adventure) {
        this.user = user;
        this.adventure = adventure;
        this.status = "started";
    }

    // JPA LIFECYCLE CALLBACKS
    /**
     * Automatically sets timestamps before persisting to database.
     */
    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // GETTERS & SETTERS
    public Long getId() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AdventureHistory{" +
                "id=" + id +
                ", user=" + user.getUserName() +
                ", adventure=" + adventure.getId() +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                '}';
    }
}
