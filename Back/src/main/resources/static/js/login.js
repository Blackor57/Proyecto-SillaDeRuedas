document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const correo = document.getElementById('correo').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ correo, password })
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Credenciales incorrectas o usuario no registrado:', errorText);
            alert('Error al iniciar sesión: ' + errorText);
            return;
        }

        const data = await response.json();
        localStorage.setItem('token', data.token);

        console.log('Inicio de sesión exitoso. Usuario autenticado.');
        alert('Inicio de sesión exitoso');
        // window.location.href = '/dashboard'; // Redirige si deseas
        window.location.href = '/view/index';
    } catch (err) {
        console.error('Error en la solicitud:', err.message);
        alert('Error al iniciar sesión: ' + err.message);
    }
});