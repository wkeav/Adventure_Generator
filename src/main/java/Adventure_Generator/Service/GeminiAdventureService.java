package Adventure_generator.Service;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.ArrayList;

/**
 * Service for generating AI-powered adventure narrations using Google Gemini Pro.
 * Uses Spring WebClient (already in pom via webflux) to call the Gemini REST API.
 */
@Service
public class GeminiAdventureService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiAdventureService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.temperature:0.7}")
    private double temperature;

    @Value("${gemini.max.tokens:1024}")
    private int maxTokens;

    @Value("${gemini.top.p:0.9}")
    private double topP;

    @Value("${gemini.top.k:40}")
    private int topK;

    private final WebClient webClient;
    private final Gson gson;

    public GeminiAdventureService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.gson = new Gson();
    }

    // Dynamic Adventure Narration 

    /**
     * Generates a narrative adventure suggestion using Gemini Pro.
     *
     * @param mood         User's mood (happy, relaxed, energetic, romantic)
     * @param weather      Weather condition (clear, rain, snow)
     * @param longDistance Whether this is for a long-distance couple
     * @return Plain text adventure narration
     */
    public String generateAdventure(String mood, String weather, boolean longDistance) {
        logger.debug("Generating adventure: mood={}, weather={}, longDistance={}", mood, weather, longDistance);
        String prompt = buildAdventurePrompt(mood, weather, longDistance);
        return callGemini(prompt);
    }

    // Mood Prediction 
    /**
     * Predicts user mood from context clues (weather, time, season).
     * Returns one of: happy, relaxed, energetic, romantic
     */
    public String predictMood(String weather, String timeOfDay, String season) {
        String prompt = String.format("""
                Based on this context, predict the most likely mood for someone planning an adventure/ date 
                
                Context:
                - Weather: %s
                - Time of day: %s
                - Season: %s
                
                Choose ONE mood from: happy, relaxed, energetic, romantic
                Respond ONLY with a JSON object: {"mood": "...", "confidence": 0.0, "reason": "..."}
                """, weather, timeOfDay, season);

        String raw = callGemini(prompt);
        return cleanJson(raw);
    }

    // Adventure Chat 
    /**
     * Chat-style interaction for adventure planning help.
     */
    public String chat(String userMessage) {
        String prompt = String.format("""
                Help users discover fun activities based on their mood, weather, and preferences.
                Keep responses concise, warm, and actionable. Suggest 1-2 specific ideas max.
                
                User: %s
                """, userMessage);

        return callGemini(prompt);
    }

    // Review Sentiment Analysis
    /**
     * Analyzes sentiment of a user-submitted adventure review and based it off for next adventures. 
     */
    public String analyzeReview(String reviewText, String adventureName) {
        String prompt = String.format("""
                Analyze this adventure review and return sentiment data.
                
                Adventure: %s
                Review: %s
                
                Respond ONLY with JSON:
                {
                  "sentiment": "positive/neutral/negative",
                  "score": 0.0,
                  "highlights": ["key phrase 1", "key phrase 2"],
                  "summary": "one sentence summary"
                }
                """, adventureName, reviewText);

        return cleanJson(callGemini(prompt));
    }

    // Generate Multiple Adventures 
    public List<String> generateMultipleAdventures(String mood, String weather, boolean longDistance, int count) {
        List<String> adventures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            adventures.add(generateAdventure(mood, weather, longDistance));
        }
        return adventures;
    }

    // Private Helpers
    private String buildAdventurePrompt(String mood, String weather, boolean longDistance) {
        String distanceContext = longDistance
                ? "This is for a long-distance couples/ friends — suggest virtual or mail-based activities."
                : "This is for people in the same location or distance.";

        String moodGuidance = switch (mood.toLowerCase()) {
            case "happy"     -> "They are joyful and playful. Suggest something fun and energizing. ";
            case "relaxed"   -> "They are calm and cozy. Suggest something soothing and low-effort.";
            case "energetic" -> "They are full of energy. Suggest something active and exciting, contains moderate or a lot of energy.";
            case "romantic"  -> "They are feeling loving and sweet. Suggest something heartfelt and intimate.";
            default          -> "Suggest a generally fun and memorable activity.";
        };

        return String.format("""
                You are a sweet and creative adventure planner.
                
                User context:
                - Mood: %s (%s)
                - Weather: %s
                - Distance: %s
                
                Write ONE adventure idea as a short engaging narrative (2-3 sentences).
                Make it feel personal and exciting. No bullet points, no JSON, just plain text.
                """, mood, moodGuidance, weather, distanceContext);
    }

    /**
     * Calls the Gemini API and returns the generated text.
     * Uses WebClient (non-blocking) but blocks for simplicity in sync controller flow.
     */
    private String callGemini(String prompt) {
        // Build request JSON from bottom up, textPart -> parts -> content ->
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", temperature);
        generationConfig.addProperty("maxOutputTokens", maxTokens);
        generationConfig.addProperty("topP", topP);
        generationConfig.addProperty("topK", topK);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contents);
        requestBody.add("generationConfig", generationConfig);

        String url = apiUrl + "?key=" + apiKey;

        try {
            String responseBody = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(gson.toJson(requestBody)) // json 
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // converts async -> sync
            return extractText(responseBody);

        } catch (Exception e) {
            logger.error("Gemini API call failed", e);
            throw new RuntimeException("Failed to call Gemini API: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts text from Gemini's nested JSON:
     * candidates[0] → content → parts[0] → text
     */
    private String extractText(String responseBody) {
        JsonObject root = gson.fromJson(responseBody, JsonObject.class);
        String text = root
                .getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        return cleanJson(text); // to get plain text
    }

    private String cleanJson(String text) {
        return text
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }
}