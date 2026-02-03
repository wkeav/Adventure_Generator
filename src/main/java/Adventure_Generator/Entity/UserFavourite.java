package Adventure_generator.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table("user_favourites")
public class UserFavourite {
    
    @Id
    private Long id; 
    private User user;
    private Adventure adventure;
    private LocalDateTime createdAt; 

    
}
