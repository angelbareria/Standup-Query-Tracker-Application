// Dashboard Page Logic

// Check authentication
requireAuth();

// Get current user
const currentUser = getCurrentUser();

// Display user name
document.getElementById('userName').textContent = currentUser.name;

// Logout function
function logout() {
    if (confirmAction('Are you sure you want to logout?')) {
        clearCurrentUser();
        window.location.href = 'index.html';
    }
}

// Load dashboard data
async function loadDashboard() {
    try {
        showLoading();

        // Fetch dashboard data for current user
        const dashboardData = await API.getUserDashboard(currentUser.id);

        // Update statistics
        document.getElementById('totalQueries').textContent = dashboardData.totalQueries;
        document.getElementById('openQueries').textContent = dashboardData.openQueries;
        document.getElementById('overdueQueries').textContent = dashboardData.overdueQueries;
        document.getElementById('myRaisedQueries').textContent = dashboardData.myRaisedQueries;
        document.getElementById('myAssignedQueries').textContent = dashboardData.myAssignedQueries;

        // Display recent queries
        displayRecentQueries(dashboardData.recentQueries);

        hideLoading();
    } catch (error) {
        hideLoading();
        showToast('Failed to load dashboard data', 'error');
        console.error('Dashboard error:', error);
    }
}

// Display recent queries in table
function displayRecentQueries(queries) {
    const tbody = document.getElementById('recentQueriesBody');

    if (!queries || queries.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="empty-state">
                    <div class="empty-state-icon">📋</div>
                    <p>No queries found</p>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = queries.map(query => `
        <tr>
            <td>
                <strong>${query.title}</strong>
                <br>
                <small style="color: var(--text-secondary);">${timeAgo(query.createdDate)}</small>
            </td>
            <td>${getTypeBadge(query.type)}</td>
            <td>${getPriorityBadge(query.priority)}</td>
            <td>${getStatusBadge(query.status)}</td>
            <td>${query.raisedBy.name}</td>
            <td>${getSLAIndicator(query)}</td>
            <td>
                <div class="query-actions">
                    <a href="query-detail.html?id=${query.id}" class="btn btn-sm btn-primary">View</a>
                </div>
            </td>
        </tr>
    `).join('');
}

// Initialize dashboard
loadDashboard();