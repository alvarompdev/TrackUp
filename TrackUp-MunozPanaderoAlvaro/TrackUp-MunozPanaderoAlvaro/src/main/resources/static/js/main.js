const token = localStorage.getItem('jwt');
if (!token) return window.location.href = '/login.html';

async function authFetch(url) {
    const r = await fetch(url, { headers: { 'Authorization': `Bearer ${token}` } });
    if (r.status === 401) return window.location.href = '/login.html';
    return r.json();
}

async function loadHabits() {
    const ul = document.getElementById('habit-list');
    const data = await authFetch('/api/habits/habits');
    data.forEach(h => {
        const li = document.createElement('li');
        li.textContent = h.nombre; // ajusta nombre de campo
        ul.appendChild(li);
    });
}

async function loadDaily() {
    const tbody = document.querySelector('#today-records tbody');
    const data = await authFetch('/api/daily-records/daily-records');
    data.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${r.fecha}</td><td>${r.habitName}</td><td>${r.valor}</td>`;
        tbody.appendChild(tr);
    });
}

async function loadGoals() {
    const ul = document.getElementById('goal-list');
    const data = await authFetch('/api/goals/goals');
    data.forEach(g => {
        const li = document.createElement('li');
        li.textContent = g.titulo; // ajusta campo
        ul.appendChild(li);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    loadHabits();
    loadDaily();
    loadGoals();
    // Aquí inicializarías Chart.js si quieres gráficos
});