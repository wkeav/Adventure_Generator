package Adventure_generator.Service;

import java.time.LocalDateTime;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Adventure_generator.Model.User;
import Adventure_generator.Repository.UserRepository;
import Adventure_generator.Util.EmailVerificationUtil;

/*
 * Handles all authentication related business logic - User registration and user login
 * Service = Business logic + security + validation 
 */

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailVerificationUtil emailVerificationUtil;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public boolean isRegisteredUser(String email){
        return userRepository.existsByEmail(email);
    }

    // User's register - validate user
    public User registerUser(String email, String password, String userName){
        if(isRegisteredUser(email.toLowerCase())){
            throw new RuntimeException("An account has already been made with this email.");
        }

        // Create new user
        User newUser = new User();
        newUser.setEmail(email.toLowerCase());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setUserName(userName);
        newUser.setCreatedAt(LocalDateTime.now());

        // Email verification 
        String emailVerification = emailVerificationUtil.generateVerificationToken();
        newUser.setEmailVerified(false);
        newUser.setVerificationToken(emailVerification);
        newUser.setVerificationTokenExpiry(emailVerificationUtil.getTokenExpire());

        return userRepository.save(newUser);

    }
    public boolean verifyEmail(String token){
        Optional<User> findUser = userRepository.findByVerificationToken(token);
        if(findUser.isPresent()){ // User exist 
            User user = findUser.get();
            if(!emailVerificationUtil.isTokenExpired(user.getVerificationTokenExpiry())){ // Before it expired
                user.setEmailVerified(true);
                user.setVerificationToken(null);
                user.setVerificationTokenExpiry(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;

    }
    public User findUserByEmail(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email.toLowerCase());
        
        if(userOptional.isPresent()){
            User user = userOptional.get();

            if(!user.isEmailVerified()){
                return null; // Can't login unless email is verified 
            }
            if(passwordEncoder.matches(password,user.getPassword())){
                return user;
            }
        }
        return null;
    }
}
