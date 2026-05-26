export class AdventureHistory {
    constructor() {
        this.token = localStorage.getItem('jwtToken');
    }

    /**
     * Fetch user's adventure statistics (completed count, average rating)
     */
    async getStats() {
        try {
            const response = await fetch('/api/history/stats', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                }
            });
            return await response.json();
        } catch (error) {
            console.error('Error fetching stats:', error);
            return null;
        }
    }

    /**
     * Fetch all user's adventure history
     */
    async getUserHistory() {
        try {
            const response = await fetch('/api/history', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                }
            });
            return await response.json();
        } catch (error) {
            console.error('Error fetching history:', error);
            return [];
        }
    }

    /**
     * Fetch completed adventures only
     */
    async getCompletedAdventures() {
        try {
            const response = await fetch('/api/history/completed', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                }
            });
            return await response.json();
        } catch (error) {
            console.error('Error fetching completed adventures:', error);
            return [];
        }
    }

    /**
     * Fetch in-progress adventures
     */
    async getInProgressAdventures() {
        try {
            const response = await fetch('/api/history/in-progress', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                }
            });
            return await response.json();
        } catch (error) {
            console.error('Error fetching in-progress adventures:', error);
            return [];
        }
    }

    /**
     * Mark an adventure as completed
     */
    async completeAdventure(historyId) {
        try {
            const response = await fetch(`/api/history/${historyId}/complete`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                }
            });
            return await response.json();
        } catch (error) {
            console.error('Error completing adventure:', error);
            return null;
        }
    }

    /**
     * Rate a completed adventure
     */
    async rateAdventure(historyId, rating, notes = '') {
        try {
            const response = await fetch(`/api/history/${historyId}/rate`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    rating: rating,
                    notes: notes
                })
            });
            return await response.json();
        } catch (error) {
            console.error('Error rating adventure:', error);
            return null;
        }
    }
}