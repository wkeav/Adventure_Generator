package Adventure_Generator.Service;

import java.util.HashMap;
import java.util.List;


import org.springframework.stereotype.Service;

@Service
public class AdventureService {

    private String [] happy_adventure = {
        "Go to a museum, and explore!",
        "Go to a comedy live show if available, if not watch a comedy show/movie! ",
        "Have a picnic! drinks on the side + paint ", 
        "Cook together ",
        "Go on a walk and yap :D "
    };
    private String [] relaxed_adventure = {
        "Visit a cafe and read some books or talk ",
        "Enjoy a spa day with relaxing treatments ",
        "Go stargazing ",
        "Crafty nights ",
        "Movie time! ", 
        "Making playlists ",
        "Crochet date "
    };
    private String [] energetic_adventure = {
        "Go hiking and enjoy nature! ",
        "Go for a run ", 
        "Rent a bike and go explore! ",
        "Try out a dance class ",
        "Online gaming " 
    };
    private String [] romantic_adventure = {
        "Take a sunset adventure ",
        "Cook together ",
        "Write each other letters ",
        "Making playlists ",
        "Visit a art gallery & hold hands hehe ",
        "Buy each other takeout ",
    };

    // Helper methods
    public String generateRandomAdventure(String mood){

        if (mood.equalsIgnoreCase("happy")){
            return getRandomAdventure(happy_adventure);
        } 
        if (mood.equalsIgnoreCase("relaxed")){
            return getRandomAdventure(relaxed_adventure);
        }
        if(mood.equalsIgnoreCase("energetic")){
            return getRandomAdventure(energetic_adventure);
        }
        if(mood.equalsIgnoreCase("romantic")){
            return getRandomAdventure(romantic_adventure);
        }
        return "Mood is invalid."; 
    }

    public String getRandomAdventure(String[] adventure){
        int index = (int) (Math.random() * adventure.length) ;
        // System.out.println(index); 
        return adventure[index];
        
    }

    public String generateAdventure(String weather, String mood){
        HashMap<String, HashMap<String, List<String>>> adventureMap = new HashMap<>();

        adventureMap.put("rain",new HashMap<happy,>());


    }

}
