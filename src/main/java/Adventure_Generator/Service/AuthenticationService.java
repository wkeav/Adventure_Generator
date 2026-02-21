package Adventure_generator.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Adventure_generator.Entity.User;
import Adventure_generator.Repository.UserRepository;

/**
 * Service layer for user authentication and registration.
 * 
 * Handles business logic for:
 * - User registration with email uniqueness validation
 * - Password encryption using BCrypt
 * - User login with credential validation
 * - Password matching for authentication
 * 
 * Security Features:
 * - BCryptPasswordEncoder for one-way password hashing
 * - Email case-insensitive comparison (stored as lowercase)
 * - Duplicate email prevention
 * - Secure password verification without exposing hashes
 */
@Service
public class AuthenticationService {
    
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Checks if a user with the given email is already registered.
     * @param email The email to check (case-insensitive)
     * @return true if email exists, false otherwise
     */
    public boolean isRegisteredUser(String email){
        return userRepository.existsByEmail(email);
    }

    /**
     * Registers a new user with encrypted password.
     * 
     * @param email User's email (will be stored as lowercase)
     * @param password Plain text password (will be BCrypt hashed)
     * @param userName User's display name
     * @return The persisted User entity
     * @throws RuntimeException if email is already registered
     */
    public User registerUser(String email, String password, String userName){
        if(isRegisteredUser(email.toLowerCase())){
            throw new RuntimeException("An account has already been made with this email.");
        }

        User newUser = new User();
        newUser.setEmail(email.toLowerCase());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setUserName(userName);
        newUser.setCreatedAt(LocalDateTime.now());

        return userRepository.save(newUser);
    }
    /**
     * Authenticates a user by email and password.
     * 
     * @param email User's email (case-insensitive)
     * @param password Plain text password to verify
     * @return User entity if credentials are valid, null otherwise
     */
    public User findUserByEmail(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email.toLowerCase());
        
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(passwordEncoder.matches(password, user.getPassword())){
                return user;
            }
        }
        return null;
    }
}
