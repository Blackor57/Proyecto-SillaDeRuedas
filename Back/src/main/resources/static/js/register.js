document.getElementById('registro-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    const nombre = document.getElementById('registro-nombre').value;
    const correo = document.getElementById('registro-correo').value;
    const password = document.getElementById('registro-password').value;

    const body = { nombre, correo, password };

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (!response.ok) throw new Error(await response.text());
        alert('Usuario registrado con éxito.');
    } catch (err) {
        alert('Error al registrarse: ' + err.message);
    }
});