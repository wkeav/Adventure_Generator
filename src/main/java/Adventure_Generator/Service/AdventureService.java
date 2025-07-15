package Adventure_generator.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import Adventure_generator.POJO.AdventureIdea;
import jakarta.annotation.PostConstruct;

@Service
public class AdventureService {
    // private String [] happy_adventure = {
    //     "Go to a museum, and explore!",
    //     "Go to a comedy live show if available, if not watch a comedy show/movie! ",
    //     "Have a picnic! drinks on the side + paint ", 
    //     "Cook together ",
    //     "Go on a walk and yap :D "
    // };
    // private String [] relaxed_adventure = {
    //     "Visit a cafe and read some books or talk ",
    //     "Enjoy a spa day with relaxing treatments ",
    //     "Go stargazing ",
    //     "Crafty nights ",
    //     "Movie time! ", 
    //     "Making playlists ",
    //     "Crochet date "
    // };
    // private String [] energetic_adventure = {
    //     "Go hiking and enjoy nature! ",
    //     "Go for a run ", 
    //     "Rent a bike and go explore! ",
    //     "Try out a dance class ",
    //     "Online gaming " 
    // };
    // private String [] romantic_adventure = {
    //     "Take a sunset adventure ",
    //     "Cook together ",
    //     "Write each other letters ",
    //     "Making playlists ",
    //     "Visit a art gallery & hold hands hehe ",
    //     "Buy each other takeout ",
    // };

    private List<AdventureIdea> adventureIdeas;

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

    public String getRandomAdventure(String[] adventure){
        int index = (int) (Math.random() * adventure.length) ;
        // System.out.println(index); 
        return adventure[index];
        
    }

    public String generateAdventure(String mood,String weather){
        // Filter the list for matching mood and weather
        List<AdventureIdea> filteredList = adventureIdeas.stream()
            .filter(a -> a.getMood().equalsIgnoreCase(mood) && a.getWeather().equalsIgnoreCase(weather))
            .toList();

            if (filteredList.isEmpty()) {
                return "No adventure found for this mood and weather!";
            }

        // Pick a random adventure 
        int index = (int) (Math.random() * filteredList.size());
        return filteredList.get(index).getAdventure();
        

    }

   

}
