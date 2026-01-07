// API Configuration
const API_CONFIG = {
    BASE_URL: '/api',
    ENDPOINTS: {
        // Auth endpoints
        LOGIN: '/auth/login',
        SIGNUP: '/auth/signup',
        USERS: '/auth/users',
        USER_BY_ID: '/auth/users',

        // Query endpoints
        QUERIES: '/queries',
        QUERY_BY_ID: '/queries',
        ASSIGN_QUERY: '/queries/{id}/assign',
        UPDATE_STATUS: '/queries/{id}/status',
        QUERIES_BY_STATUS: '/queries/status',
        QUERIES_BY_TYPE: '/queries/type',
        QUERIES_RAISED_BY: '/queries/raised-by',
        QUERIES_ASSIGNED_TO: '/queries/assigned-to',
        OVERDUE_QUERIES: '/queries/overdue',
        SEARCH_QUERIES: '/queries/search',

        // Dashboard endpoints
        DASHBOARD: '/dashboard',
        USER_DASHBOARD: '/dashboard/user',
        DASHBOARD_STATS: '/dashboard/stats'
    }
};

// Enums
const QUERY_TYPE = {
    BLOCKER: 'BLOCKER',
    QUERY: 'QUERY',
    DEPENDENCY: 'DEPENDENCY',
    FOLLOWUP: 'FOLLOWUP'
};

const PRIORITY = {
    HIGH: 'HIGH',
    MEDIUM: 'MEDIUM',
    LOW: 'LOW'
};

const STATUS = {
    OPEN: 'OPEN',
    IN_PROGRESS: 'IN_PROGRESS',
    RESOLVED: 'RESOLVED',
    CLOSED: 'CLOSED'
};

// Status colors
const STATUS_COLORS = {
    OPEN: '#3b82f6',           // Blue
    IN_PROGRESS: '#f59e0b',    // Orange
    RESOLVED: '#10b981',       // Green
    CLOSED: '#6b7280'          // Gray
};

// Priority colors
const PRIORITY_COLORS = {
    HIGH: '#ef4444',           // Red
    MEDIUM: '#f59e0b',         // Orange
    LOW: '#10b981'             // Green
};

// SLA status colors
const SLA_COLORS = {
    ON_TRACK: '#10b981',       // Green
    WARNING: '#f59e0b',        // Orange
    CRITICAL: '#ef4444',       // Red
    OVERDUE: '#991b1b',        // Dark Red
    COMPLETED: '#6b7280'       // Gray
};

// Local storage keys
const STORAGE_KEYS = {
    USER: 'standup_tracker_user',
    TOKEN: 'standup_tracker_token'
};