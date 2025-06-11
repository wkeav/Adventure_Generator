// Handle adventure generation and API calls
export class AdventureGenerator {
    constructor() {
        this.initializeGenerateButton();
    }

    initializeGenerateButton() {
        const generateBtn = document.getElementById('generate-btn');
        generateBtn.addEventListener('click', (e) => this.handleGenerateClick(e));
    }

    async handleGenerateClick(event) {
        event.preventDefault();
        // We'll implement the AJAX call here
        // This is where you'll add your fetch or axios call
    }
} 