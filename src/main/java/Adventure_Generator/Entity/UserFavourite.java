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

@Entity
@Table(
    name = "user_favourites",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "adventure_id"}),
    indexes = { // B tree indexes 
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_adventure_id", columnList = "adventure_id")
    }

)
public class UserFavourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="adventure_id", nullable=false)
    private Adventure adventure;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt; 

    // CONSTRUCTOR 
    public UserFavourite(){}

    public UserFavourite(User user, Adventure adventure){
        this.user = user;
        this.adventure = adventure; 
    }

    // Getter & setter
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

    // JPA callbacks
    /**
     * Called automatically by JPA before the entity is persisted to the database
     */
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    // Help methods 
    @Override
    public String toString() {
        return "UserFavourite [id=" + id + ", user=" + user + ", adventure=" + adventure + ", createdAt=" + createdAt
                + "]";
    }

    
}
