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

    

    }
} 