package Adventure_generator.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.User;
import Adventure_generator.Entity.UserFavourite;
import Adventure_generator.Repository.UserFavoriteRepository;

/**
 * 
 */
@Service
public class FavouriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavouriteService.class);
    private final UserFavoriteRepository userFavoriteRepository;

    public FavouriteService(UserFavoriteRepository userFavoriteRepository){
        this.userFavoriteRepository = userFavoriteRepository;
    }

    /**
     * Toggle favourite — if already favourited, remove it. If not, add it.
     * @return true if added, false if removed
     */
    @Transactional
    public boolean toggleFavourite(User user, Adventure adventure) {
        Optional<UserFavourite> existing = userFavoriteRepository.findByUserAndAdventure(user, adventure);

        if(existing.isPresent()){
            userFavoriteRepository.delete(existing.get());
            logger.debug("Removed favourite for user={} adventure={}", user.getUserName(), adventure.getId());
            return false; // removed
        }else {
            UserFavourite favourite = new UserFavourite(user, adventure);
            userFavoriteRepository.save(favourite);
            logger.debug("Added favourite for user={} adventure={}", user.getUserName(), adventure.getId());
            return true; // added
        }
    }

    /**
     * Get all favourites for a user.
     */
    @Transactional
    public List<UserFavourite> getUserFavourites(Long userId){
        return userFavoriteRepository.findAllByUserId(userId);
    }

    /**
     * Check if a specific adventure is already favourited by the user.
     */
    public boolean isFavourited(User user, Adventure adventure){
        return userFavoriteRepository.findByUserAndAdventure(user, adventure).isPresent();
    }

}
