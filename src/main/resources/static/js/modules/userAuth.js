// Handle user registers or login 
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
            const response = await fetch('/api/auth/register', {
                method: "POST",
                headers:{
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({email,password,confirmPassword,userName})
            });
            if(!response.ok){
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json(); //convert to js object 
            // Store user's data and token if success 
            if(data.success && data.token){
                this.saveToken(data.token); 
                this.saveUserData(data.userData);
            }
            return data;
        }catch(error){
            return {success: false, message: "Failed to register. Please try again."};
        }
    }

    async login(email,password){
        try{
            const response = await fetch('/api/auth/login', {
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
                            }, 1500);
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
                this.startLoading();

                const email = document.getElementById('login-email').value;
                const password = document.getElementById('login-password').value;
                const messageDiv = document.getElementById('register-message');

                try{
                    const result = await this.login(email,password);
                    if(result.success){
                        messageDiv.textContent = "Login successful!";
                        messageDiv.style.color = "green";
                        setTimeout(() => {
                            window.location.href = "/home.html"; 
                        }, 1500);
                    }else{
                        messageDiv.textContent = result.message || "Login failed. Please try again.";
                        messageDiv.style.color = "red";
                    }
                }finally{
                    this.stopLoading(loginBtn);
                }
            }
    };
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

        // Correct email format 
        const checkEmail = () => {
            const emailPattern = /^[^@]+@[^@]+\.[^@]+$/;
            if(emailInput.value && !emailPattern.test(emailInput.value)){
                messageDiv.textContent = "Please enter a valid email address.";
                messageDiv.style.color = "red";
            }else{
                messageDiv.textContent = "";
            }
        }
        emailInput.addEventListener('input', checkEmail);
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