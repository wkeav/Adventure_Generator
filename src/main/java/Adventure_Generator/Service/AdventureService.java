package Adventure_generator.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import Adventure_generator.Model.Adventure;
import Adventure_generator.Model.User;
import Adventure_generator.POJO.AdventureIdea;
import Adventure_generator.Repository.AdventureRepository;
import jakarta.annotation.PostConstruct;

/**
 * Service for generating and persisting adventure recommendations
 * Loads adventure ideas from adventures.json and filters by mood/weather
 */
@Service
public class AdventureService {

    private List<AdventureIdea> adventureIdeas;

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
        }catch (IOException e){
            e.printStackTrace();
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

        if(filteredList.isEmpty()){
            return "No adventure found for this mood, weather, and preference!";
        }

        // Generate random adventure and return
        int index = (int)(Math.random() * filteredList.size());
        return filteredList.get(index).getAdventure();
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
        Adventure adventure = new Adventure(adventureText, user, mood, weather, isLongDistance, false);
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