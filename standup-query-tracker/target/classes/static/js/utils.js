// Utility Functions

// Format date to readable string
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Calculate time ago
function timeAgo(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMins / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (diffMins < 60) return `${diffMins} minutes ago`;
    if (diffHours < 24) return `${diffHours} hours ago`;
    return `${diffDays} days ago`;
}

// Get status badge HTML
function getStatusBadge(status) {
    const color = STATUS_COLORS[status] || '#6b7280';
    return `<span class="badge" style="background-color: ${color};">${status.replace('_', ' ')}</span>`;
}

// Get priority badge HTML
function getPriorityBadge(priority) {
    const color = PRIORITY_COLORS[priority] || '#6b7280';
    return `<span class="badge badge-priority" style="background-color: ${color};">${priority}</span>`;
}

// Get type badge HTML
function getTypeBadge(type) {
    const colors = {
        BLOCKER: '#dc2626',
        QUERY: '#2563eb',
        DEPENDENCY: '#7c3aed',
        FOLLOWUP: '#059669'
    };
    const color = colors[type] || '#6b7280';
    return `<span class="badge badge-type" style="background-color: ${color};">${type}</span>`;
}

// Calculate SLA status
function getSLAStatus(query) {
    if (!query.slaDeadline) return 'NO_SLA';

    const status = query.status;
    if (status === 'RESOLVED' || status === 'CLOSED') return 'COMPLETED';

    const deadline = new Date(query.slaDeadline);
    const now = new Date();
    const hoursRemaining = (deadline - now) / (1000 * 60 * 60);

    if (hoursRemaining < 0) return 'OVERDUE';
    if (hoursRemaining <= 4) return 'CRITICAL';
    if (hoursRemaining <= 12) return 'WARNING';
    return 'ON_TRACK';
}

// Get SLA indicator HTML
function getSLAIndicator(query) {
    const slaStatus = getSLAStatus(query);
    const color = SLA_COLORS[slaStatus];
    const labels = {
        ON_TRACK: '✓ On Track',
        WARNING: '⚠ Warning',
        CRITICAL: '🔥 Critical',
        OVERDUE: '❌ Overdue',
        COMPLETED: '✓ Completed',
        NO_SLA: 'No SLA'
    };
    return `<span class="sla-indicator" style="color: ${color};">${labels[slaStatus]}</span>`;
}

// Show toast notification
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('show');
    }, 100);

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Show loading overlay
function showLoading() {
    const loader = document.createElement('div');
    loader.id = 'loading-overlay';
    loader.innerHTML = '<div class="spinner"></div>';
    document.body.appendChild(loader);
}

// Hide loading overlay
function hideLoading() {
    const loader = document.getElementById('loading-overlay');
    if (loader) loader.remove();
}

// Confirm dialog
function confirmAction(message) {
    return confirm(message);
}

// Get user from localStorage
function getCurrentUser() {
    const userStr = localStorage.getItem(STORAGE_KEYS.USER);
    return userStr ? JSON.parse(userStr) : null;
}

// Save user to localStorage
function saveCurrentUser(user) {
    localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(user));
}

// Clear user from localStorage
function clearCurrentUser() {
    localStorage.removeItem(STORAGE_KEYS.USER);
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
}

// Check if user is authenticated
function isAuthenticated() {
    return getCurrentUser() !== null;
}

// Redirect to login if not authenticated
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = 'index.html';
    }
}