// Handle mood selection and state
/**
 * Mood Handler Module
 * 
 * Manages user mood selection interface and state.
 * Provides mood buttons for happy, relaxed, energetic, and romantic moods.
 * 
 * Features:
 * - Interactive mood button selection
 * - Visual feedback for selected mood
 * - Mood state management
 * - Integration with AdventureGenerator
 * 
 * Available Moods:
 * - happy
 * - relaxed
 * - energetic
 * - romantic
 * 
 * Dependencies:
 * - DOM elements with data-mood attributes
 * 
 * @module MoodHandler
 * @author Astra K. Nguyen
 * @version 1.0.0
 */

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

        // Only show the selected one
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