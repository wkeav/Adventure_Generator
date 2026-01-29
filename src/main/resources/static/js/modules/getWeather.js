
/**
 * Weather Service Module
 * 
 * Handles fetching and displaying current weather information based on user's geolocation.
 * Requires JWT authentication to access the weather API endpoint.
 * 
 * Features:
 * - Gets user's current location via browser Geolocation API
 * - Fetches weather data from backend /api/weather endpoint with JWT token
 * - Updates UI with temperature, description, city name, feels-like temp, and weather icon
 * 
 * Dependencies:
 * - Requires valid JWT token in localStorage for authenticated requests
 * - Backend WeatherController and OpenWeatherMap API integration
 * - DOM elements: weather-temp, weather-description, weather-city, weather-feels-like, weather-icon
 * 
 * @module getWeather
 * @requires localStorage
 * @version 1.0.0
 */

export class getWeather {
    getLocation(){
        if('geolocation' in navigator){
            navigator.geolocation.getCurrentPosition(
            (position) => {
                const latitude = position.coords.latitude;
                const longitude = position.coords.longitude;
                console.log(`Latitude: ${latitude}, Longitude: ${longitude}`);

                this.fetchWeather(latitude,longitude);
            },
            (error) => {
                console.error("Error getting location:", error.message);
            }
    )}else {
        console.log("Geolocation is not supported by this browser.");
    }
};

    fetchWeather(lat, lon){
        const token = localStorage.getItem('jwtToken');
        
        if (!token) {
            console.error('No JWT token found. User must be logged in to fetch weather.');
            return;
        }
        
        fetch(`/api/weather?lat=${lat}&lon=${lon}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                const tempElement = document.getElementById('weather-temp');
                const descElement = document.getElementById('weather-description');
                const cityElement = document.getElementById('weather-city');
                const feelsLikeElement = document.getElementById('weather-feels-like');
                const iconElement = document.getElementById('weather-icon');

                // Update their content with backend data
                if (tempElement) tempElement.textContent = Math.round(data.temperature) + "°C";
                if (descElement) descElement.textContent = data.description;
                if (cityElement) cityElement.textContent = data.cityName;
                if (feelsLikeElement) feelsLikeElement.textContent = "Feels like: " + Math.round(data.feelLike) + "°C";
                if (iconElement) {
                    iconElement.src = `https://openweathermap.org/img/wn/${data.icon}@2x.png`;
                    iconElement.alt = data.description;
                }
            })
            .catch(error =>{
                console.error("Error fetching weather:", error);
                const descElement = document.getElementById('weather-description');
                if (descElement) descElement.textContent = "Unable to load weather";
            })
    }
}

