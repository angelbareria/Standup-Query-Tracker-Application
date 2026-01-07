// Queries Page Logic

// Check authentication
requireAuth();

// Get current user
const currentUser = getCurrentUser();
document.getElementById('userName').textContent = currentUser.name;

// Store all queries
let allQueries = [];

// Logout function
function logout() {
    if (confirmAction('Are you sure you want to logout?')) {
        clearCurrentUser();
        window.location.href = 'index.html';
    }
}

// Load all queries
async function loadQueries() {
    try {
        showLoading();
        allQueries = await API.getAllQueries();
        displayQueries(allQueries);
        hideLoading();
    } catch (error) {
        hideLoading();
        showToast('Failed to load queries', 'error');
        console.error('Load queries error:', error);
    }
}

// Display queries in table
function displayQueries(queries) {
    const tbody = document.getElementById('queriesBody');

    if (!queries || queries.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="10" class="empty-state">
                    <div class="empty-state-icon">📋</div>
                    <p>No queries found</p>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = queries.map(query => `
        <tr>
            <td>#${query.id}</td>
            <td>
                <strong>${query.title}</strong>
            </td>
            <td>${getTypeBadge(query.type)}</td>
            <td>${getPriorityBadge(query.priority)}</td>
            <td>${getStatusBadge(query.status)}</td>
            <td>${query.raisedBy.name}</td>
            <td>${query.assignedTo ? query.assignedTo.name : '<em>Unassigned</em>'}</td>
            <td>${getSLAIndicator(query)}</td>
            <td>
                <small>${timeAgo(query.createdDate)}</small>
            </td>
            <td>
                <div class="query-actions">
                    <a href="query-detail.html?id=${query.id}" class="btn btn-sm btn-primary">View</a>
                </div>
            </td>
        </tr>
    `).join('');
}

// Apply filters
function applyFilters() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase().trim();
    const statusFilter = document.getElementById('statusFilter').value;
    const typeFilter = document.getElementById('typeFilter').value;
    const priorityFilter = document.getElementById('priorityFilter').value;

    let filteredQueries = allQueries;

    // Search filter
    if (searchTerm) {
        filteredQueries = filteredQueries.filter(q =>
            q.title.toLowerCase().includes(searchTerm) ||
            (q.description && q.description.toLowerCase().includes(searchTerm))
        );
    }

    // Status filter
    if (statusFilter) {
        filteredQueries = filteredQueries.filter(q => q.status === statusFilter);
    }

    // Type filter
    if (typeFilter) {
        filteredQueries = filteredQueries.filter(q => q.type === typeFilter);
    }

    // Priority filter
    if (priorityFilter) {
        filteredQueries = filteredQueries.filter(q => q.priority === priorityFilter);
    }

    displayQueries(filteredQueries);
}

// Clear filters
function clearFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('statusFilter').value = '';
    document.getElementById('typeFilter').value = '';
    document.getElementById('priorityFilter').value = '';
    displayQueries(allQueries);
}

// Real-time search
document.getElementById('searchInput').addEventListener('input', applyFilters);

// Initialize
loadQueries();