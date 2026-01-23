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
import Adventure_generator.Model.User;
import Adventure_generator.Service.AuthenticationService;
import Adventure_generator.Util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    
    public AuthController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

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
            User newUser = authenticationService.registerUser(email,password,userName); // Create new registered user 

            AuthResponse authResponse = new AuthResponse(true, "Registration successful! Please check your email to verify your account.", null, null, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

        } catch(Exception e){
            log.error("Registration failed", e);
            AuthResponse errorAuthResponse = new AuthResponse(false, "Registration failed: " + e.getMessage(), null, null, "REGISTRATION_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorAuthResponse);
        }
    }
    
    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
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
    
    @GetMapping(value = "/verify-email", produces = "text/plain")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try{
            if(token == null || token.isEmpty()){
                return ResponseEntity.badRequest().body("Verification token is required.");
            }

            boolean isEmailVerified = authenticationService.verifyEmail(token);
            if(isEmailVerified){
                return ResponseEntity.ok("Email verified successfully! You can now log in.");
            }else{
                return ResponseEntity.badRequest().body("Invalid or expired verification token.");
            }

        } catch (Exception e){
            log.error("Email verification failed for token: " + token,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during email verification.");
        }
    }
    

} 