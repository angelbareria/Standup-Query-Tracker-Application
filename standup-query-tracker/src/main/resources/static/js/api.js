// API Service Layer

const API = {
    // Helper function to make API calls
    async request(endpoint, method = 'GET', body = null) {
        const url = `${API_CONFIG.BASE_URL}${endpoint}`;
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json'
            }
        };

        if (body) {
            options.body = JSON.stringify(body);
        }

        try {
            const response = await fetch(url, options);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || data.message || 'Request failed');
            }

            return data;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    // Auth APIs
    async login(email, password) {
        return await this.request(API_CONFIG.ENDPOINTS.LOGIN, 'POST', { email, password });
    },

    async signup(name, email, password, role) {
        return await this.request(API_CONFIG.ENDPOINTS.SIGNUP, 'POST', { name, email, password, role });
    },

    async getAllUsers() {
        return await this.request(API_CONFIG.ENDPOINTS.USERS);
    },

    async getUserById(id) {
        return await this.request(`${API_CONFIG.ENDPOINTS.USER_BY_ID}/${id}`);
    },

    // Query APIs
    async getAllQueries() {
        return await this.request(API_CONFIG.ENDPOINTS.QUERIES);
    },

    async getQueryById(id) {
        return await this.request(`${API_CONFIG.ENDPOINTS.QUERY_BY_ID}/${id}`);
    },

    async createQuery(queryData) {
        return await this.request(API_CONFIG.ENDPOINTS.QUERIES, 'POST', queryData);
    },

    async updateQuery(id, queryData) {
        return await this.request(`${API_CONFIG.ENDPOINTS.QUERY_BY_ID}/${id}`, 'PUT', queryData);
    },

    async assignQuery(id, userId) {
        return await this.request(
            API_CONFIG.ENDPOINTS.ASSIGN_QUERY.replace('{id}', id),
            'PUT',
            { userId }
        );
    },

    async updateQueryStatus(id, status) {
        return await this.request(
            API_CONFIG.ENDPOINTS.UPDATE_STATUS.replace('{id}', id),
            'PUT',
            { status }
        );
    },

    async deleteQuery(id) {
        return await this.request(`${API_CONFIG.ENDPOINTS.QUERY_BY_ID}/${id}`, 'DELETE');
    },

    async getQueriesByStatus(status) {
        return await this.request(`${API_CONFIG.ENDPOINTS.QUERIES_BY_STATUS}/${status}`);
    },

    async getQueriesByType(type) {
        return await this.request(`${API_CONFIG.ENDPOINTS.QUERIES_BY_TYPE}/${type}`);
    },

    async getQueriesRaisedBy(userId) {
        return await this.request(`${API_CONFIG.ENDPOINTS.QUERIES_RAISED_BY}/${userId}`);
    },

    async getQueriesAssignedTo(userId) {
        return await this.request(`${API_CONFIG.ENDPOINTS.QUERIES_ASSIGNED_TO}/${userId}`);
    },

    async getOverdueQueries() {
        return await this.request(API_CONFIG.ENDPOINTS.OVERDUE_QUERIES);
    },

    async searchQueries(keyword) {
        return await this.request(`${API_CONFIG.ENDPOINTS.SEARCH_QUERIES}?keyword=${encodeURIComponent(keyword)}`);
    },

    // Dashboard APIs
    async getDashboard() {
        return await this.request(API_CONFIG.ENDPOINTS.DASHBOARD);
    },

    async getUserDashboard(userId) {
        return await this.request(`${API_CONFIG.ENDPOINTS.USER_DASHBOARD}/${userId}`);
    },

    async getDashboardStats() {
        return await this.request(API_CONFIG.ENDPOINTS.DASHBOARD_STATS);
    }
};