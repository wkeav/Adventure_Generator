// Main entry point
import { MoodHandler } from './modules/moodHandler.js';
import { AdventureGenerator } from './modules/adventureGenerator.js';
import { getWeather } from './modules/getWeather.js';
import{userAuth} from './modules/userAuth.js';

document.addEventListener('DOMContentLoaded', () => {

    if (document.getElementById('generate-btn')) {
        const moodHandler = new MoodHandler();
        const adventureGenerator = new AdventureGenerator();
        adventureGenerator.setMood(moodHandler);
    }

    
        const weather = new getWeather();
        weather.getLocation();

    // Always initialize userAuth 
    const auth = new userAuth();
    auth.RegisterValidation();
    auth.registerForm();
    auth.loginForm();
}); 
