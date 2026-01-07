// Query Detail Page Logic

// Check authentication
requireAuth();

// Get current user
const currentUser = getCurrentUser();
document.getElementById('userName').textContent = currentUser.name;

// Get query ID from URL
const urlParams = new URLSearchParams(window.location.search);
const queryId = urlParams.get('id');

if (!queryId) {
    showToast('Invalid query ID', 'error');
    setTimeout(() => window.location.href = 'queries.html', 1500);
}

let currentQuery = null;
let allUsers = [];

// Logout function
function logout() {
    if (confirmAction('Are you sure you want to logout?')) {
        clearCurrentUser();
        window.location.href = 'index.html';
    }
}

// Load query details
async function loadQueryDetails() {
    try {
        showLoading();

        // Fetch query and users in parallel
        [currentQuery, allUsers] = await Promise.all([
            API.getQueryById(queryId),
            API.getAllUsers()
        ]);

        displayQueryDetails();
        populateUserSelect();

        hideLoading();
    } catch (error) {
        hideLoading();
        showToast('Failed to load query details', 'error');
        console.error('Load query error:', error);
        setTimeout(() => window.location.href = 'queries.html', 2000);
    }
}

// Display query details
function displayQueryDetails() {
    // Set title
    document.getElementById('queryTitle').textContent = currentQuery.title;

    // Set basic info
    document.getElementById('queryId').textContent = `#${currentQuery.id}`;
    document.getElementById('queryType').innerHTML = getTypeBadge(currentQuery.type);
    document.getElementById('queryPriority').innerHTML = getPriorityBadge(currentQuery.priority);
    document.getElementById('queryStatus').innerHTML = getStatusBadge(currentQuery.status);
    document.getElementById('queryDescription').textContent = currentQuery.description || 'No description provided';

    // Set timeline
    document.getElementById('createdDate').textContent = formatDate(currentQuery.createdDate);
    document.getElementById('lastUpdated').textContent = formatDate(currentQuery.lastUpdated);
    document.getElementById('slaDeadline').innerHTML = currentQuery.slaDeadline
        ? formatDate(currentQuery.slaDeadline) + '<br>' + getSLAIndicator(currentQuery)
        : 'Not set';

    // Show resolved info if resolved
    if (currentQuery.resolvedDate) {
        document.getElementById('resolvedItem').style.display = 'flex';
        document.getElementById('resolvedDate').textContent = formatDate(currentQuery.resolvedDate);
        document.getElementById('resolutionTime').textContent =
            `Resolved in ${currentQuery.resolutionTimeHours} hours`;
    }

    // Set people
    document.getElementById('raisedBy').textContent = currentQuery.raisedBy.name;
    document.getElementById('assignedTo').textContent = currentQuery.assignedTo
        ? currentQuery.assignedTo.name
        : 'Unassigned';

    // Set current status in dropdown
    document.getElementById('statusSelect').value = currentQuery.status;
}

// Populate user select dropdown
function populateUserSelect() {
    const select = document.getElementById('assignUserSelect');
    select.innerHTML = '<option value="">Select user...</option>';

    allUsers.forEach(user => {
        const option = document.createElement('option');
        option.value = user.id;
        option.textContent = `${user.name} (${user.role})`;

        // Pre-select if already assigned
        if (currentQuery.assignedTo && currentQuery.assignedTo.id === user.id) {
            option.selected = true;
        }

        select.appendChild(option);
    });
}

// Assign user to query
async function assignUser() {
    const userId = document.getElementById('assignUserSelect').value;

    if (!userId) {
        showToast('Please select a user', 'warning');
        return;
    }

    try {
        showLoading();
        await API.assignQuery(queryId, userId);
        hideLoading();

        showToast('User assigned successfully', 'success');

        // Reload query details
        await loadQueryDetails();
    } catch (error) {
        hideLoading();
        showToast(error.message || 'Failed to assign user', 'error');
        console.error('Assign user error:', error);
    }
}

// Update query status
async function updateStatus() {
    const newStatus = document.getElementById('statusSelect').value;

    if (newStatus === currentQuery.status) {
        showToast('Status is already set to ' + newStatus, 'warning');
        return;
    }

    try {
        showLoading();
        await API.updateQueryStatus(queryId, newStatus);
        hideLoading();

        showToast('Status updated successfully', 'success');

        // Reload query details
        await loadQueryDetails();
    } catch (error) {
        hideLoading();
        showToast(error.message || 'Failed to update status', 'error');
        console.error('Update status error:', error);
    }
}

// Delete query
async function deleteQuery() {
    if (!confirmAction('Are you sure you want to delete this query? This action cannot be undone.')) {
        return;
    }

    try {
        showLoading();
        await API.deleteQuery(queryId);
        hideLoading();

        showToast('Query deleted successfully', 'success');

        // Redirect to queries page
        setTimeout(() => {
            window.location.href = 'queries.html';
        }, 1000);
    } catch (error) {
        hideLoading();
        showToast(error.message || 'Failed to delete query', 'error');
        console.error('Delete query error:', error);
    }
}

// Initialize
loadQueryDetails();