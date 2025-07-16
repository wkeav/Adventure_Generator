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
        console.log('Generate button clicked!');
        event.preventDefault();
        const selectedMood = this.mood.getSelectedMood();

        if(!selectedMood){
            alert('Please select a mood first!')
            return;
        }

        // AJAX with fetch API 
        try{
            this.startLoading();
            const response = await fetch('/api/adventures/generate',{
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

            // Update UI with backend data 
            const adventureElement = document.getElementById('adventure-description');
            const moodElement = document.getElementById('mood-text');
            // console.log('Found elements:', {adventureElement, moodElement}); 

            adventureElement.textContent = data.adventureIdea;
            moodElement.textContent = selectedMood.charAt(0).toUpperCase() + selectedMood.slice(1);

            document.getElementById('adventure-result').classList.remove('hidden');
            
        }catch(error){
            console.error('Error:', error);
            alert('Failed to generate adventure. Please try again.');
        }finally {
            this.stopLoading();
        }
    }
} 