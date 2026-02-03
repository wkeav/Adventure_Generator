package Adventure_generator.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import Adventure_generator.Model.Adventure;
import Adventure_generator.Model.User;
import Adventure_generator.POJO.AdventureIdea;
import Adventure_generator.Repository.AdventureRepository;
import jakarta.annotation.PostConstruct;

/**
 * Adventure Service
 * 
 * Business logic layer for adventure generation and management.
 * Loads adventure ideas from JSON file and filters based on user preferences.
 * Persists generated adventures to database with user associations.
 * 
 * Features:
 * - Loads adventure data from adventures.json on application startup
 * - Filters adventures by mood, weather conditions, and distance preference
 * - Random selection from filtered adventure pool
 * - Saves generated adventures with user metadata
 * - Retrieves user's adventure history
 * 
 * Adventure Filtering:
 * - Mood: happy, relaxed, energetic, romantic
 * - Weather: clear, rain, snow, any
 * - Distance: regular or long-distance
 * 
 * @author Astra K. Nguyen
 * @version 1.0.0
 * @since 2026-01-28
 */
@Service
public class AdventureService {

    private List<AdventureIdea> adventureIdeas;
    private static final Logger logger = LoggerFactory.getLogger(AdventureService.class);

    @Autowired
    private AdventureRepository adventureRepository;

    /**
     * Load adventure ideas from JSON file on startup.
     */
    @PostConstruct
    public void loadAdventures(){
        ObjectMapper mapper = new ObjectMapper(); // Use to convert between Java objects and JSON

        try(InputStream is = getClass().getResourceAsStream("/adventures.json")){ // Open and read inputStream 
            adventureIdeas = Arrays.asList(mapper.readValue(is, AdventureIdea[].class)); // Convert into an arraylist
            logger.info("Successfully loaded {} adventures from adventures.json", adventureIdeas.size());
        }catch (IOException e){
            e.printStackTrace();
            logger.error("Failed to load adventures.json: {}", e.getMessage());
            adventureIdeas = new ArrayList<>(); // Empty array list
        }
    }

    /**
     * Generate a random adventure based on mood, weather, and distance preference.
     * 
     * @param mood the user's current mood
     * @param weather the current weather condition
     * @param longDistance whether it's a long-distance adventure
     * @return adventure suggestion text
     */
    public String generateAdventure(String mood, String weather, boolean longDistance){
        logger.debug("generateAdventure called with mood={}, weather={}, longDistance={}", mood, weather, longDistance);
        logger.debug("Total adventures loaded: {}", adventureIdeas.size());
        
        List<AdventureIdea> filteredList = adventureIdeas.stream()
            .filter(a -> a.getMood().equalsIgnoreCase(mood) && (a.getWeather().equalsIgnoreCase(weather) || a.getWeather().equalsIgnoreCase("any")))
            .filter(a -> {
                String distance = a.getDistance();
                if(longDistance){
                    return "long-distance".equalsIgnoreCase(distance);
                }else{
                    return distance == null;
                }
            })
            .toList();

        logger.debug("Filtered adventures count: {}", filteredList.size());
        
        if(filteredList.isEmpty()){
            logger.warn("No adventure found for mood={}, weather={}, longDistance={}", mood, weather, longDistance);
            return "No adventure found for this mood, weather, and preference!";
        }

        // Generate random adventure and return
        int index = (int)(Math.random() * filteredList.size());
        String selectedAdventure = filteredList.get(index).getAdventure();
        logger.debug("Selected adventure: {}", selectedAdventure);
        return selectedAdventure;
    }
    
    /**
     * Save a generated adventure to the database.
     * 
     * @param adventureText the adventure description
     * @param user the user who generated this adventure
     * @param mood the mood used for generation
     * @param weather the weather condition
     * @param isLongDistance whether it's a long-distance adventure
     * @return the saved Adventure entity
     */
    public Adventure saveAdventure(String adventureText, User user, String mood, String weather, Boolean isLongDistance){
        Adventure adventure = new Adventure(adventureText, user, mood, weather, isLongDistance);
        return adventureRepository.save(adventure);
    }

    /**
     * Get all adventures for a specific user, ordered by newest first.
     * 
     * @param userId the user's ID
     * @return list of user's adventures
     */
    public List<Adventure> getUserAdventures(Long userId) {
        return adventureRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}