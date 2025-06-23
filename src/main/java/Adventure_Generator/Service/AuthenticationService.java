package Adventure_Generator.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Adventure_Generator.Repository.UserRepository;

/*
 * Handles all authentication related business logic - User registration and user login
 * Service = Business logic + security + validation 
 */

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // TODO finish all this 
    public boolean isRegisteredUser(String email){
    }

    public User registerUser(String email, String password, String userName){
    }

    public String findUserByEmail(String email, String password){
    }
    
    
}
