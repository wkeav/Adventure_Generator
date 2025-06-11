// Main entry point
import { MoodHandler } from './modules/moodHandler.js';
import { AdventureGenerator } from './modules/adventureGenerator.js';

document.addEventListener('DOMContentLoaded', () => {
    const moodHandler = new MoodHandler();
    const adventureGenerator = new AdventureGenerator();

    adventureGenerator.setMood(moodHandler);
}); 