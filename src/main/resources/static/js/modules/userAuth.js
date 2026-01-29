/**
 * User Authentication Module
 * 
 * Handles user registration, login, and JWT token management.
 * Manages authentication state and redirects users between login and home pages.
 * 
 * Features:
 * - User registration with password confirmation validation
 * - User login with JWT token generation
 * - Token and user data storage in localStorage
 * - Form validation with real-time feedback
 * - Loading states for async operations
 * - Automatic redirect after successful authentication
 * 
 * API Endpoints:
 * - POST /api/auth/registrations - User registration
 * - POST /api/auth/sessions - User login
 * 
 * Dependencies:
 * - Backend AuthController for authentication
 * - localStorage for token persistence
 * - DOM elements: login-form, register-form, message divs
 * 
 * @module userAuth
 * @requires localStorage
 * @author Astra K. Nguyen
 * @version 1.0.0
 */

export class userAuth {
    startLoading(button) {
        if (button) {
            button.disabled = true;
            button.dataset.originalText = button.textContent;
            button.textContent = "Loading...";
        }
    }
    
    stopLoading(button) {
        if (button) {
            button.disabled = false;
            button.textContent = button.dataset.originalText || "Submit";
            delete button.dataset.originalText;
        }
    }
    async register(email,password,confirmPassword,userName){
        try{
            const response = await fetch('/api/auth/registrations', {
                method: "POST",
                headers:{
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({email,password,confirmPassword,userName})
            });
            
            const data = await response.json(); //convert to js object 
            
            // Return data regardless of status code - let the caller handle success/failure
            if(data.success && data.token){
                try {
                    this.saveToken(data.token); 
                    this.saveUserData(data.userData);
                } catch (storageError) {
                    console.error("Storage error:", storageError);
                }
            }
            return data;
        }catch(error){
            console.error("Registration error:", error);
            return {success: false, message: "Failed to register. Please try again."};
        }
    }

    async login(email,password){
        try{
            const response = await fetch('/api/auth/sessions', {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({email,password})
            });
            if(!response.ok){
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json(); //convert to js object 
            if (data.success && data.token) {
                this.saveToken(data.token);
                this.saveUserData(data.userData);
            }
            return data;
        }catch(error){
            return {success: false, message: "Failed to login. Please try again."};
        }
    }

    registerForm(){
            const registerForm = document.getElementById('register-form'); 
            if(registerForm){ // if user is on the page 
                this.RegisterValidation();
                registerForm.onsubmit = async(e) => {
                    e.preventDefault();
                    const registerBtn = registerForm.querySelector('button[type="submit"]');
                    this.startLoading(registerBtn);

                    const username = document.getElementById('register-username').value.trim();
                    const email = document.getElementById('register-email').value.trim();
                    const password = document.getElementById('register-password').value;
                    const confirmPassword = document.getElementById('register-confirm-password').value;
                    const messageDiv = document.getElementById('register-message');

                //     const emailPattern = /^[^@]+@[^@]+\.[^@]+$/;
                // if (!emailPattern.test(email)) {
                //     messageDiv.textContent = "Please enter a valid email address.";
                //     messageDiv.style.color = "red";
                //     this.stopLoading(registerBtn);
                //     return;
                // }
                // if (password !== confirmPassword) {
                //     messageDiv.textContent = "Passwords do not match.";
                //     messageDiv.style.color = "red";
                //     this.stopLoading(registerBtn);
                //     return;
                // }
                    
                    try{
                        const result  = await this.register(email, password, confirmPassword, username);
                        if(result.success){
                            messageDiv.textContent = "Registration successful!";
                            messageDiv.style.color = "green";
                            setTimeout(() => {
                                window.location.href = "/home.html"; 
                            }, 2000);
                        }else{
                            messageDiv.textContent = result.message || "Registration failed. Please try again.";
                            messageDiv.style.color = "red";
                        }
                    }finally{
                        this.stopLoading(registerBtn);
                    }
                };
            
            };
    }

    loginForm(){
        const loginForm = document.getElementById('login-form');
        if(loginForm){
            loginForm.onsubmit = async(e) => {
                e.preventDefault();
                const loginBtn = loginForm.querySelector('button[type="submit"]');
                this.startLoading(loginBtn);

                const email = document.getElementById('login-email').value;
                const password = document.getElementById('login-password').value;
                const messageDiv = document.getElementById('login-message');

                try{
                    const result = await this.login(email,password);
                    if(result.success){
                        messageDiv.textContent = "Login successful! Redirecting...";
                        messageDiv.style.color = "green";
                        
                        // Redirect after a short delay
                        setTimeout(() => {
                            window.location.href = "/home.html"; 
                        }, 1500);
                    }else{
                        messageDiv.textContent = result.message || "Invalid password or email. Please try again.";
                        messageDiv.style.color = "red";
                    }
                }finally{
                    this.stopLoading(loginBtn);
                }
            };
        }
    }

    RegisterValidation(){
        const passwordInput = document.getElementById('register-password');
        const confirmPasswordInput = document.getElementById('register-confirm-password');
        const emailInput = document.getElementById('register-email');
        const messageDiv = document.getElementById('register-message');

        // Password match check 
        const checkPasswordsMatch = () => {
            if(passwordInput.value != confirmPasswordInput.value){
                messageDiv.textContent = "Passwords do not match.";
                messageDiv.style.color = "red";
            }else{
                messageDiv.textContent = "";
            }
        }
        passwordInput.addEventListener('input', checkPasswordsMatch); // real-time check 
        confirmPasswordInput.addEventListener('input', checkPasswordsMatch);

        // // Correct email format 
        // const checkEmail = () => {
        //     const emailPattern = /^[^@]+@[^@]+\.[^@]+$/;
        //     if(emailInput.value && !emailPattern.test(emailInput.value)){
        //         messageDiv.textContent = "Please enter a valid email address.";
        //         messageDiv.style.color = "red";
        //     }else{
        //         messageDiv.textContent = "";
        //     }
        // }
        // emailInput.addEventListener('input', checkEmail);
    }

    saveToken(token){
        localStorage.setItem('jwtToken', token); //token already a string
    }
    saveUserData(userData){
        localStorage.setItem('userData', JSON.stringify(userData));
    }
    getToken(){
        return localStorage.getItem('jwtToken');
    }
    logout(){
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('userData');
    }
}