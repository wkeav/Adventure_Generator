package Adventure_generator.Controller;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import Adventure_generator.DTOs.Requests.LoginRequest;
import Adventure_generator.DTOs.Requests.RegisterRequest;
import Adventure_generator.DTOs.Response.AuthResponse;
import Adventure_generator.DTOs.Response.UserData;
import Adventure_generator.Entity.User;
import Adventure_generator.Service.AuthenticationService;
import Adventure_generator.Util.JwtUtil;

/**
 * REST controller for user authentication operations.
 * 
 * Provides HTTP endpoints for:
 * - POST /api/auth/registrations - User registration with validation
 * - POST /api/auth/sessions - User login with JWT token generation
 * 
 * Security:
 * - Publicly accessible (no JWT required for login/register)
 * - Password validation and BCrypt encryption via AuthenticationService
 * - JWT token generation on successful authentication
 * - Email uniqueness validation
 * 
 * Response Flow:
 * 1. Client sends registration/login request
 * 2. Server validates input (Jakarta Bean Validation)
 * 3. AuthenticationService processes business logic
 * 4. JwtUtil generates token if successful
 * 5. AuthResponse returned with token and user data
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user account.
     * 
     * Validates password match, creates user with encrypted password,
     * generates JWT token, and returns auth response with user data.
     * 
     * @param registerRequest Contains email, password, confirmPassword, userName
     * @return ResponseEntity with AuthResponse (token + user data) or error
     */
    @PostMapping(value = "/registrations", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String userName = registerRequest.getUserName();

        try{
            if(!registerRequest.isPasswordMatching()){
                AuthResponse errorAuthResponse = new AuthResponse(false, "Password doesn't match. Try again.", null,null, "PASSWORD_MISMATCH");
                return ResponseEntity.badRequest().body(errorAuthResponse);
            }
            User newUser = authenticationService.registerUser(email, password, userName);
            UserData userData = new UserData( 
                newUser.getId(),
                newUser.getEmail(),
                newUser.getUserName(),
                newUser.getCreatedAt()
            );
            String token = jwtUtil.generateToken(userData);
            AuthResponse authResponse = new AuthResponse(true, "Registration successful!", token, userData, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

        } catch(Exception e){
            log.error("Registration failed", e);
            AuthResponse errorAuthResponse = new AuthResponse(false, "Registration failed: " + e.getMessage(), null, null, "REGISTRATION_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorAuthResponse);
        }
    }
    
    /**
     * Authenticates existing user and generates JWT token.
     * 
     * Validates credentials, retrieves user data, generates JWT token,
     * and returns auth response for successful login.
     * 
     * @param loginRequest Contains email and password
     * @return ResponseEntity with AuthResponse (token + user data) or 401 unauthorized
     */
    @PostMapping(value = "/sessions", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

      try{
        User existingUser = authenticationService.findUserByEmail(email, password);

        if(existingUser != null){
            UserData userData = new UserData(
            existingUser.getId(),
            existingUser.getEmail(),
            existingUser.getUserName(),
            existingUser.getCreatedAt()
            );
            String token = jwtUtil.generateToken(userData);
            AuthResponse authResponse = new AuthResponse(true, "Login successful", token, userData, null);

            return ResponseEntity.ok(authResponse);
        }else{
            AuthResponse errorAuthResponse = new AuthResponse(false, "Invalid email or password", null,null, "INVALID_CREDENTIALS");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorAuthResponse);
        }

      }catch(Exception e){
        log.error("Login failed", e);
        AuthResponse errorAuthResponse = new AuthResponse(false, "Login failed: " + e.getMessage(), null,null, "LOGIN_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorAuthResponse);
      }
    }
    
} 