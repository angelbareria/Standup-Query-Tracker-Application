// Create Query Page Logic

// Check authentication
requireAuth();

// Get current user
const currentUser = getCurrentUser();
document.getElementById('userName').textContent = currentUser.name;

// Logout function
function logout() {
    if (confirmAction('Are you sure you want to logout?')) {
        clearCurrentUser();
        window.location.href = 'index.html';
    }
}

// Handle form submission
document.getElementById('createQueryForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const title = document.getElementById('title').value.trim();
    const description = document.getElementById('description').value.trim();
    const type = document.getElementById('type').value;
    const priority = document.getElementById('priority').value;

    if (!title || !type || !priority) {
        showToast('Please fill in all required fields', 'error');
        return;
    }

    const queryData = {
        title,
        description,
        type,
        priority,
        raisedById: currentUser.id
    };

    try {
        showLoading();
        const createdQuery = await API.createQuery(queryData);
        hideLoading();

        showToast('Query created successfully!', 'success');

        // Redirect to query detail page
        setTimeout(() => {
            window.location.href = `query-detail.html?id=${createdQuery.id}`;
        }, 1000);
    } catch (error) {
        hideLoading();
        showToast(error.message || 'Failed to create query', 'error');
        console.error('Create query error:', error);
    }
});