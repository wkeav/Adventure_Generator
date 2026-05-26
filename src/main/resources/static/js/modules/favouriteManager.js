export class FavouriteManager {
    constructor(){
        this.token = localStorage.getItem('jwtToken');
    }

    /**
     * Toggle favourite status of an adventure
     */
    async toggleFavoutite(adventureId){
        try{
            const response = await fetch(`/api/favourites/${adventureId}/toggle`, {
                method: 'POST',
                headers:{
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                }
            });
            return await response.json();
        } catch (error){
            console.error('Error toggling favourite:', error);
            return{success: false, error: error.message};
        }
    }
    /**
     * Check if an adventure is favourited
     */
    async isFavourited(adventureId){
        try{
            const response = await fetch(`/api/favourites/check/${adventureId}`, {
                method:'GET',
                headers: {
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                }
            });
            const data = await response.json();
            return data.isFavourited;
        } catch (error){
            console.error('Error checking favourite:', error);
            return{sucess: false, error: error.message};
        }
    }

    /**
     * Get all user's favourite adventures
     */
    async getUserFavourites(){
        try{
            const response = await fetch('/api/favourites/user', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.token}`,
                    'Content-Type': 'application/json'
                }
            });
            return await response.json();
        } catch (error){
            console.error('Error fetching favourites:', error);
            return [];
        }
    }

}