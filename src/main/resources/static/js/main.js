// Main entry point
import { MoodHandler } from './modules/moodHandler.js';
import { AdventureGenerator } from './modules/adventureGenerator.js';
import { getWeather } from './modules/getWeather.js';

document.addEventListener('DOMContentLoaded', () => {
    const moodHandler = new MoodHandler();
    const adventureGenerator = new AdventureGenerator();
    const weather = new getWeather();

    adventureGenerator.setMood(moodHandler);
    weather.getLocation();

}); 
