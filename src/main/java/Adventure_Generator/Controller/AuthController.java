package Adventure_Generator.Controller;

import Adventure_Generator.DTOs.Requests.RegisterRequest;
import Adventure_Generator.Model.User;
import Adventure_Generator.Service.AuthenticationService;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        //TODO: process POST request

        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String confirmedPassword = registerRequest.getConfirmPassword();
        String userName = registerRequest.getUserName();

        try{
            if(!registerRequest.isPasswordMatching()){
               return ResponseEntity.badRequest().body("Password doesn't match. Try again.");
            }
            User registerUser = authenticationService.registerUser(email,password,userName);

            // Return response 
            Map<String,Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User has registered successfully.");
            Map<String,Object> userData = new HashMap<>();
            userData.put("userId", registerUser.getId());
            userData.put("email",registerUser.getEmail());
            userData.put("userName", registerUser.getUserName());

            userData.put("data", userData);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch(Exception e){
            Map<String,Object> responseError = new HashMap<>();
            responseError.put("success", false);
            responseError.put("message", "Password do not match.");
            responseError.put("error", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError);
        }
    }
    


} 