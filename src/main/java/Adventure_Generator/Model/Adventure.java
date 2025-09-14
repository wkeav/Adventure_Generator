package Adventure_generator.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/*
 * This class represent a generated adventure for a user 
 */
@Entity
@Table(name = "adventures")
public class Adventure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Adventure is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String adventures;
    
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user; 

    @NotBlank(message = "Mood is required")
    @Column(nullable = false, length = 20)
    private String mood;

    @NotBlank(message = "Weather is required")
    @Column(nullable = false )
    private String weather;

    @Column(name = "is_long_distance")
    private Boolean isLongDistance = false; 

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // TODO: finish this file 
}
