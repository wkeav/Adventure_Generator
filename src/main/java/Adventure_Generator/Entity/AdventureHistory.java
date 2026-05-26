package Adventure_generator.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a user's adventure history record.
 * Tracks when adventures were started, completed, and their ratings.
 */
@Entity
@Table(name = "adventure_history", indexes = {
    @Index(name = "idx_status", columnList = "status")
})
public class AdventureHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "adventure_id", nullable = false)
    private Adventure adventure;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // "started", "completed"

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "rating")
    private Integer rating; // 1-5 scale

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Adventure getAdventure() { return adventure; }
    public void setAdventure(Adventure adventure) { this.adventure = adventure; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
