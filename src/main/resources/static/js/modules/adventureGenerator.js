// Handle adventure generation and API calls
export class AdventureGenerator {
    constructor() {
        this.initializeGenerateButton();
        this.mood = null;
    }
    setMood(mood){
        this.mood = mood;
    }

    initializeGenerateButton() {
        const generateBtn = document.getElementById('generate-btn');
        generateBtn.addEventListener('click', (e) => this.generateClick(e));
    }

    startLoading(){
        const generateBtn = document.getElementById('generate-btn');
        generateBtn.disabled = true;
        generateBtn.innerHTML = `
        <span class="material-symbols-outlined animate-spin">refresh</span>
        Generating... `;
    }
    stopLoading(){
        const generateBtn = document.getElementById('generate-btn');
        generateBtn.disabled = false;
        generateBtn.innerHTML = 'Generate New Adventure';
        
    }

    async generateClick(event) {
        event.preventDefault();
        const selectedMood = this.mood.getSelectedMood();

        if(!selectedMood){
            alert('Please select a mood first!')
            return;
        }

        // AJAX with fetch API 
        try{
            this.startLoading();
            const response = await fetch('/generate-adventure',{
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json'
                },
                body:JSON.stringify({mood: selectedMood})
            });
            if(!response.ok){
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json(); // response data 
        }catch(error){
            console.error('Error:', error);
            alert('Failed to generate adventure. Please try again.');
        }finally {
            this.stopLoading();
        }

    

    }
} 