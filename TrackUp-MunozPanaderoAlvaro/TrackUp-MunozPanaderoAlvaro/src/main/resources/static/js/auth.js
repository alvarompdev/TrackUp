const API = '/api/auth';

document.addEventListener('DOMContentLoaded', () => {
    // Registro
    const rf = document.getElementById('register-form');
    if (rf) rf.addEventListener('submit', async e => {
        e.preventDefault();
        const data = Object.fromEntries(new FormData(rf));
        const res = await fetch(`${API}/register`, {
            method:'POST', headers:{'Content-Type':'application/json'},
            body:JSON.stringify(data)
        });
        if (res.ok) window.location.href='login.html';
        else document.getElementById('register-error').textContent = await res.text();
    });

    // Login
    const lf = document.getElementById('login-form');
    if (lf) lf.addEventListener('submit', async e => {
        e.preventDefault();
        const data = Object.fromEntries(new FormData(lf));
        const res = await fetch(`${API}/login`, {
            method:'POST', headers:{'Content-Type':'application/json'},
            body:JSON.stringify(data)
        });
        if (!res.ok) {
            document.getElementById('login-error').textContent = 'Credenciales inválidas';
            return;
        }
        const { jwt } = await res.json();
        localStorage.setItem('jwt', jwt);
        window.location.href = '/';
    });
});