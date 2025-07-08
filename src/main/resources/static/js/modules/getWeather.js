

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
        fetch(`/api/weather?lat=${lat}&lon=${lon}`)
            .then(response => response.json())
            .then(data => {
                const tempElement = document.getElementById('weather-temp');
                const descElement = document.getElementById('weather-description');
                const cityElement = document.getElementById('weather-city');
                const feelsLikeElement = document.getElementById('weather-feels-like');
                const iconElement = document.getElementById('weather-icon');

                // Update their content with backend data
                tempElement.textContent = Math.round(data.temperature) + "°C";
                descElement.textContent = data.description;
                cityElement.textContent = data.cityName;
                feelsLikeElement.textContent = "Feels like: " + Math.round(data.feelLike) + "°C";
                iconElement.src = `https://openweathermap.org/img/wn/${data.icon}@2x.png`;
                iconElement.alt = data.description;
            })
            .catch(error =>{
                console.error("Error fetching weather:", error);
            })
    }
}

