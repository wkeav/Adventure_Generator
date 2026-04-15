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
        this.initializeAiToggle(); // add this
        this.mood = null;
    }
    setMood(mood){
        this.mood = mood;
    }

    initializeGenerateButton() {
        const generateBtn = document.getElementById('generate-btn');
        generateBtn.addEventListener('click', (e) => this.generateClick(e));
    }

    initializeAiToggle() {
        const aiToggle = document.getElementById('ai-toggle');
        const hint = document.getElementById('ai-toggle-hint');
        if (!aiToggle || !hint) return;

        aiToggle.addEventListener('change', () => {
            if (aiToggle.checked) {
                hint.textContent = 'Our AI will craft your adventure';
                hint.classList.remove('text-gray-400');
                hint.classList.add('text-purple-500');
            } else {
                hint.textContent = 'Using curated adventures';
                hint.classList.remove('text-purple-500');
                hint.classList.add('text-gray-400');
            }
        });
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

        const token = localStorage.getItem('jwtToken'); 
        const weatherDesc = document.getElementById('weather-description')?.textContent?.toLowerCase() || '';
        const longDistance = localStorage.getItem('longDistancePref') === 'true';
        const aiToggle = document.getElementById('ai-toggle');

        // Map weather description to adventures JSON 
        let weather = 'clear';
        if (weatherDesc.includes('rain') || weatherDesc.includes('drizzle') || weatherDesc.includes('shower') || weatherDesc.includes('mist') || weatherDesc.includes('broken clouds')) {
            weather = 'rain';
        } else if (weatherDesc.includes('snow')) {
            weather = 'snow';
        } else if (weatherDesc.includes('clear') || weatherDesc.includes('sunny') || weatherDesc.includes('sky')) {
            weather = 'clear';
        } else {
            weather = 'any';
        }

        try {
            const response = await fetch('/api/adventures/generate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` 
                },
                body: JSON.stringify({
                    mood: selectedMood,
                    weather: weather,
                    longDistance: longDistance,
                    aiGenerated: aiToggle ? aiToggle.checked : false
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            console.log('Adventure generated:', data);

            // Update UI with result
            const adventureDesc = document.getElementById('adventure-description');
            const moodBadge = document.getElementById('mood-badge');
            if (adventureDesc) adventureDesc.textContent = data.adventure || data.message;
            if (moodBadge) moodBadge.textContent = selectedMood;

        } catch(error) {
            console.error('Error:', error);
        }
    }
}

/**
 * Adventure Chat Module
 * 
 * Handles sending and receiving messages for adventure chat.
 * Communicates with backend API to process user messages and generate replies.
 * 
 * Features:
 * - Sends user messages to backend for processing
 * - Receives and displays replies from backend
 * - Requires JWT authentication for all requests
 * 
 * API Endpoints:
 * - POST /api/adventures/chat - Send message and receive reply
 * 
 * Dependencies:
 * - JWT token from localStorage
 * - Backend AdventureController and AdventureService
 * 
 * @module AdventureChat
 * @requires localStorage
 * @author Astra K. Nguyen
 * @version 1.0.0
 */

export class AdventureChat {
    constructor() {
        this.token = localStorage.getItem('jwtToken');
    }

    async sendMessage(message) {
        const response = await fetch('/api/adventures/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify({ message })
        });
        const data = await response.json();
        return data.reply;
    }
}