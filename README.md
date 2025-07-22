# Adventure Generator 🌟

A personalized adventure recommendation platform that helps couples and friends discover meaningful activities based on their mood and the weather conditions. 

## Overview

Adventure Generator is a full-stack web application that combines mood-based recommendations with real-time weather data to suggest perfect activities for couples and friends. Whether you're together or miles apart, find your next memorable adventure!

## Features

### Current Features
- **Mood-Based Adventures:** Choose from happy, relaxed, energetic, or romantic moods
- **Weather Integration:** Real-time weather data affects adventure suggestions
- **Long-Distance Support:** Virtual adventures for remote partners and friends
- **User Authentication:** Secure registration and login with JWT tokens
- **Beautiful UI:** Modern, responsive design with smooth animations

### Planned Features
- **Partner Invitation System:** Connect with friends and romantic partners(Coming soon!)
- **Adventure Blogging:** Share your experiences with photos and stories(Coming soon!)
- **Social Features:** Comment and interact with other users' adventures(Coming soon!)
- **Adventure History:** Track your completed adventures and favorites(Coming soon!)
- **Advanced Filtering:** Budget, distance, and activity type preferences(Coming soon!)

## Technology Stack

### Backend
- **Java 17** with **Spring Boot 3.x**
- **Spring Security** with JWT authentication
- **JPA/Hibernate** for database operations
- **PostgreSQL** for production (H2 for development)
- **Maven** for dependency management
- **Jackson** for JSON processing

### Frontend
- **Vanilla JavaScript** (ES6+ modules)
- **HTML5** with semantic structure
- **CSS3** with Tailwind CSS framework
- **Material Symbols** for icons
- **Responsive Design** for all devices

### External APIs
- **OpenWeather API:** Real-time weather data
- **Geolocation API:** User location detection

## Getting Started!

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or use H2 for development)
- OpenWeather API key (free at [openweathermap.org](https://openweathermap.org/api))

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/adventure-generator.git
   cd adventure-generator
   ```

2. **Set up the database**
   ```bash
   # For PostgreSQL
   createdb adventure_generator
   
   # Or use H2 (no setup required)
   ```

3. **Configure application properties**
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
   
   Update the following properties:
   ```properties
   # Database configuration
   spring.datasource.url=jdbc:postgresql://localhost:5432/adventure_generator
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # OpenWeather API
   openweather.api.key=your_api_key_here
   
   # JWT Secret
   jwt.secret=your_jwt_secret_here
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Open your browser to `http://localhost:8080`
   - Register a new account or use existing credentials

## Usage

### Basic Workflow
1. **Register/Login** to create your account
2. **Select your mood** (happy, relaxed, energetic, romantic)
3. **Toggle preferences** (long-distance adventures on/off)
4. **Generate adventure** and get personalized recommendations
5. **Enjoy your adventure!**

### API Usage
{Api__Document} Coming soon!

## Project Structure

```
Adventure_Generator/
├── src/main/java/Adventure_generator/
│   ├── Controller/          # REST API endpoints
│   ├── Service/            # Business logic
│   ├── Model/              # JPA entities
│   ├── Repository/         # Data access layer
│   ├── DTOs/              # Request/Response objects
│   ├── Config/            # Security and configuration
│   └── Util/              # Helper utilities
├── src/main/resources/
│   ├── static/            # Frontend assets
│   │   ├── js/modules/    # JavaScript modules
│   │   └── styles.css     # Styling
│   ├── adventures.json    # Adventure data
│   └── application.properties
└── target/                # Compiled artifacts
```

## Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Production Build
```bash
mvn clean package
java -jar target/adventure-generator-1.0.0.jar
```

### Docker (Optional)
```bash
# Build image
docker build -t adventure-generator .

# Run container
docker run -p 8080:8080 adventure-generator
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Astra Ng.**
- LinkedIn: [Astra's LinkedIn](www.linkedin.com/in/astra-n-40b024259)
- Email: astra.k.nguyen05@outlook.com


## Acknowledgments

- **OpenWeather API** for weather data
- **Material Design** for icon system
- **Tailwind CSS** for styling framework
- **Spring Boot Community** for excellent documentation

--- 
*Created by Astra Nguyen - January 2025*