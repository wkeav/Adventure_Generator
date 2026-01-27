package Adventure_generator.Model;

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

import Adventure_generator.Model.User;

/*
 * Adventure Entity - Represents a generated adventure for a user, can be modified (favourite, notes, etc)
 * 
 * Database Table: adventure
 * Relationships: Many adventures belong to one user
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

    @Column(name="is_favourite")
    private Boolean isFavourite = false;

    // CONSTRUCTOR
    public Adventure(){}

    public Adventure(String adventure, User user, String mood, String weather, Boolean isLongDistance, Boolean isFavourite) {
        this.adventure = adventure;
        this.user = user;
        this.mood = mood;
        this.weather = weather;
        this.isLongDistance = isLongDistance != null ? isLongDistance : false;
        this.isFavourite = false;
    }

    // JPA CALLBACKS
    /**
     * Called automatically by JPA before the entity is persisted to the database
     */
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Called automatically by JPA 
     */
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    // GETTER & SETTER
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

    public Boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean isFavourite){
        this.isFavourite = isFavourite != null ? isFavourite : false;
    }

    // OBJECT METHODS
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
