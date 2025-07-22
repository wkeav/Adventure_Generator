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
    if (document.getElementById('register-form')) {
        auth.RegisterValidation();
    }
    auth.registerForm();
    auth.loginForm();

    // Show user profile if on home page
    const userProfileDiv = document.getElementById('user-profile');
    if (userProfileDiv) {
        const userData = localStorage.getItem('userData');
        let userName = '';
        if (userData) {
            try {
                const user = JSON.parse(userData);
                userName = user.userName;
            } catch (e) {
                userName = '';
            }
        }
        console.log('userName:', userName);
        userProfileDiv.innerHTML = userName
            ? `<span class="material-symbols-outlined" style="color: black;">account_circle</span><span class="font-semibold" style="color: black;">${userName}</span>`
            : '<span style="color: black;">Not logged in</span>';
    }

    // Long distance preference
    const longDistancePref = document.getElementById('long-distance-pref');
    if(longDistancePref){
        const distancePref = localStorage.getItem('longDistancePref');
        longDistancePref.checked = distancePref === 'true'; // Set the checkbox to true or false 

        longDistancePref.addEventListener('change', (e) => {
            localStorage.setItem('longDistancePref', e.target.checked);
        });
    }


}); 
