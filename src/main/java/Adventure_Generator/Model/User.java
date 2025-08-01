package Adventure_generator.Model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * This is a JPA entity, that maps directly to the database 
 */

@Entity
@Table(name = "\"users\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true, nullable=false)
    private String email;
    @Column(nullable=false, unique=true)
    private String userName;
    @Column(nullable=false)
    private String password;
    @Column
    private LocalDateTime createdAt;
    @Column(name="email_is_verified", nullable=false)
    private boolean emailVerified;
    @Column(name="verification_token")
    private String verificationToken;
    @Column(name="verification_token_expiry")
    private LocalDateTime  verificationTokenExpiry;
    

    public User(){
    }
    

    public User(long id, String email, String userName, String password, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.createdAt = createdAt;
    }
    // Getter & setter
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", userName=" + userName + ", password=" + password
                + ", createdAt=" + createdAt + ", emailVerified=" + emailVerified + ", verificationToken="
                + verificationToken + ", verificationTokenExpiry=" + verificationTokenExpiry + "]";
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }


    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }


    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }


    public LocalDateTime getVerificationTokenExpiry() {
        return verificationTokenExpiry;
    }


    public void setVerificationTokenExpiry(LocalDateTime verificationTokenExpiry) {
        this.verificationTokenExpiry = verificationTokenExpiry;
    }

}
