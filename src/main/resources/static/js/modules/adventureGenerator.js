/**
 * Adventure Generator Module
 * 
 * Handles adventure generation based on user's mood, weather conditions, and distance preferences.
 * Communicates with backend API to generate personalized adventure suggestions.
 * 
 * Features:
 * - Generates adventures based on mood selection
 * - Integrates with weather data for context-aware suggestions
 * - Supports long-distance preference toggle
 * - Displays generated adventures with loading states
 * - Requires JWT authentication for all requests
 * 
 * API Endpoints:
 * - POST /api/adventures/generate - Generate new adventure
 * 
 * Dependencies:
 * - MoodHandler module for mood selection
 * - JWT token from localStorage
 * - Backend AdventureController and AdventureService
 * - DOM elements: generate-btn, adventure-description, mood-text, adventure-result
 * 
 * @module AdventureGenerator
 * @requires localStorage
 * @requires MoodHandler
 * @author Astra K. Nguyen
 * @version 1.0.0
 */

export class AdventureGenerator {
    constructor() {
        this.initializeGenerateButton();
        this.mood = null;
    }
    setMood(mood){
        this.mood = mood;
    }

    initializeGenerateButton() {
        const generateBtn = document.getElementById('generate-btn');
        generateBtn.addEventListener('click', (e) => this.generateClick(e));
    }

    startLoading(){
        const generateBtn = document.getElementById('generate-btn');
        generateBtn.disabled = true;
        generateBtn.innerHTML = `
        <span class="material-symbols-outlined animate-spin">refresh</span>
        Generating... `;
    }
    stopLoading(){
        const generateBtn = document.getElementById('generate-btn');
        generateBtn.disabled = false;
        generateBtn.innerHTML = 'Generate New Adventure';
        
    }

    async generateClick(event) {
        console.log('Generate button clicked!');
        event.preventDefault();
        const selectedMood = this.mood.getSelectedMood();

        if(!selectedMood){
            alert('Please select a mood first!')
            return;
        }

        const weatherDesc = document.getElementById('weather-description')?.textContent?.toLowerCase() || '';
        const longDistance = localStorage.getItem('longDistancePref') === 'true';

        // Map weather description to adventures JSON 
        let weather = 'clear'; // default 
        if (weatherDesc.includes('rain') || weatherDesc.includes('drizzle') || weatherDesc.includes('shower') || weatherDesc.includes('mist')) {
            weather = 'rain';
        } else if (weatherDesc.includes('snow')) {
            weather = 'snow';
        } else if (weatherDesc.includes('clear') || weatherDesc.includes('sunny') || weatherDesc.includes('sky')) {
            weather = 'clear';
        } else {
            weather = 'any'; 
        }

        // AJAX with fetch API 
        try{
            this.startLoading();
            const token = localStorage.getItem('jwtToken');
            if (!token) {
                alert('Please log in first!');
                window.location.href = '/login.html';
                return;
            }
            
            const response = await fetch('/api/adventures/generate',{
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body:JSON.stringify({
                    mood: selectedMood,
                    weather: weather,
                    longDistance: longDistance
                })
            });
            if(!response.ok){
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json(); // response data 

            const adventureElement = document.getElementById('adventure-description');
            const moodElement = document.getElementById('mood-text');
            // console.log('Found elements:', {adventureElement, moodElement}); 

            // Update UI with backend data 
            adventureElement.textContent = data.adventureIdea;
            moodElement.textContent = selectedMood.charAt(0).toUpperCase() + selectedMood.slice(1);

            document.getElementById('adventure-result').classList.remove('hidden');
            
        }catch(error){
            console.error('Error:', error);
            alert('Failed to generate adventure. Please try again.');
        }finally {
            this.stopLoading();
        }
    }
} 