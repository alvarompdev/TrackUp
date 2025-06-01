// Funciones de utilidad
function showAlert(message, type = 'success') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.querySelector('.container').insertBefore(alertDiv, document.querySelector('.container').firstChild);
}

function showLoading() {
    const loadingDiv = document.createElement('div');
    loadingDiv.className = 'loading-overlay';
    loadingDiv.innerHTML = `
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Cargando...</span>
        </div>
    `;
    document.body.appendChild(loadingDiv);
}

function hideLoading() {
    const loadingDiv = document.querySelector('.loading-overlay');
    if (loadingDiv) {
        loadingDiv.remove();
    }
}

// Funciones para manejar hábitos
async function saveHabit(habitData) {
    try {
        showLoading();
        const response = await fetch('/api/habits', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(habitData)
        });

        if (!response.ok) {
            throw new Error('Error al guardar el hábito');
        }

        showAlert('Hábito guardado exitosamente');
        window.location.reload();
    } catch (error) {
        showAlert(error.message, 'danger');
    } finally {
        hideLoading();
    }
}

async function deleteHabit(id) {
    try {
        showLoading();
        const response = await fetch(`/api/habits/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Error al eliminar el hábito');
        }

        showAlert('Hábito eliminado exitosamente');
        window.location.reload();
    } catch (error) {
        showAlert(error.message, 'danger');
    } finally {
        hideLoading();
    }
}

// Funciones para manejar metas
async function saveGoal(goalData) {
    try {
        showLoading();
        const response = await fetch('/api/goals', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(goalData)
        });

        if (!response.ok) {
            throw new Error('Error al guardar la meta');
        }

        showAlert('Meta guardada exitosamente');
        window.location.reload();
    } catch (error) {
        showAlert(error.message, 'danger');
    } finally {
        hideLoading();
    }
}

async function deleteGoal(id) {
    try {
        showLoading();
        const response = await fetch(`/api/goals/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Error al eliminar la meta');
        }

        showAlert('Meta eliminada exitosamente');
        window.location.reload();
    } catch (error) {
        showAlert(error.message, 'danger');
    } finally {
        hideLoading();
    }
}

// Funciones para manejar registros diarios
async function saveRecord(recordData) {
    try {
        showLoading();
        const response = await fetch('/api/records', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(recordData)
        });

        if (!response.ok) {
            throw new Error('Error al guardar el registro');
        }

        showAlert('Registro guardado exitosamente');
        window.location.reload();
    } catch (error) {
        showAlert(error.message, 'danger');
    } finally {
        hideLoading();
    }
}

async function deleteRecord(id) {
    try {
        showLoading();
        const response = await fetch(`/api/daily-records/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Error al eliminar el registro');
        }

        showAlert('Registro eliminado exitosamente');
        window.location.reload();
    } catch (error) {
        showAlert(error.message, 'danger');
    } finally {
        hideLoading();
    }
}

// Funciones para manejar el perfil
async function updateProfile(profileData) {
    try {
        showLoading();
        const response = await fetch('/api/profile', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(profileData)
        });

        if (!response.ok) {
            throw new Error('Error al actualizar el perfil');
        }

        showAlert('Perfil actualizado exitosamente');
    } catch (error) {
        showAlert(error.message, 'danger');
    } finally {
        hideLoading();
    }
}

// Inicialización
document.addEventListener('DOMContentLoaded', function() {
    // Inicializar tooltips de Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Inicializar popovers de Bootstrap
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}); 