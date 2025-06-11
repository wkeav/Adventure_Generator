// Handle mood selection and state
export class MoodHandler {
    constructor() {
        this.selectedMood = null;
        this.initializeMoodListeners();
    }

    initializeMoodListeners() {
        const moodRadios = document.querySelectorAll('.mood-radio');
        moodRadios.forEach(radio => {
            radio.addEventListener('change', (e) => this.handleMoodChange(e));
        });
    }

    handleMoodChange(event) {
        // Hide all selected badges
        document.querySelectorAll('.mood-selected').forEach(el => {
            el.style.display = 'none';
        });

        if (event.target.checked) {
            const selectedBadge = event.target.parentNode.querySelector('.mood-selected');
            if (selectedBadge) {
                selectedBadge.style.display = 'block';
                this.selectedMood = event.target.value;
                console.log('Selected mood:', this.selectedMood);
            }
        }
    }

    getSelectedMood() {
        return this.selectedMood;
    }
} 