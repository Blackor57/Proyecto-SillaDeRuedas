const grid = document.getElementById('grid');
let placing = 'start';
let startNode = null;
let endNode = null;
const rows = 20;
const cols = 30;
let mapaYaGuardado = false;

function construirMapaParaBackend() {
    const nodos = [];
    const aristas = [];
    const nodoIds = {};
    let idCounter = 0;

    for (let y = 0; y < rows; y++) {
        for (let x = 0; x < cols; x++) {
            const cell = document.querySelector(`td[data-x="${x}"][data-y="${y}"]`);
            if (!cell.classList.contains('wall')) {
                const id = 'n' + idCounter++;
                nodoIds[`${x},${y}`] = id;
                nodos.push({ identificador: id, x, y });
            }
        }
    }

    const directions = [[0,1], [1,0], [0,-1], [-1,0]];

    nodos.forEach(n => {
        directions.forEach(([dx, dy]) => {
            const nx = n.x + dx;
            const ny = n.y + dy;
            const neighborKey = `${nx},${ny}`;
            const neighborCell = document.querySelector(`td[data-x="${nx}"][data-y="${ny}"]`);

            if (
                neighborCell &&
                !neighborCell.classList.contains('wall') &&
                nodoIds[neighborKey]
            ) {
                aristas.push({
                    origenId: n.identificador,
                    destinoId: nodoIds[neighborKey],
                    costo: 1
                });
            }
        });
    });

    return { nodos, aristas };
}



async function guardarMapaEnBackend() {
    const mapa = construirMapaParaBackend();
    const token = localStorage.getItem('token');

    if (!token) {
        alert('No hay token JWT disponible. Inicia sesión primero.');
        console.log('Token enviado:', token);
        return;
    }
    try {
        const response = await fetch('/api/ruta/guardar-mapa', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            body: JSON.stringify(mapa)
        });

        if (!response.ok) {
            throw new Error('Error al guardar el mapa');
        }

        console.log('Mapa guardado');
    } catch (err) {
        alert('Error al guardar el mapa: ' + err.message);
    }
}

function createGrid() {
    for (let y = 0; y < rows; y++) {
        const row = document.createElement('tr');
        for (let x = 0; x < cols; x++) {
            const cell = document.createElement('td');
            cell.dataset.x = x;
            cell.dataset.y = y;
            cell.addEventListener('click', () => handleClick(cell));
            row.appendChild(cell);
        }
        grid.appendChild(row);
    }
}

function handleClick(cell) {
    if (placing === 'start') {
        if (startNode) startNode.classList.remove('start');
        cell.classList.add('start');
        startNode = cell;
        placing = 'end';
    } else if (placing === 'end') {
        if (endNode) endNode.classList.remove('end');
        cell.classList.add('end');
        endNode = cell;
        placing = 'wall';
    } else {
        if (!cell.classList.contains('start') && !cell.classList.contains('end')) {
            cell.classList.toggle('wall');
            mapaYaGuardado = false; 
        }
    }
}

function resetGrid() {
    grid.innerHTML = '';
    startNode = null;
    endNode = null;
    placing = 'start';
    mapaYaGuardado = false;
    createGrid();
}

function clearPath() {
    document.querySelectorAll('.path').forEach(cell => cell.classList.remove('path'));
}


async function startVisualization() {
    if (!startNode || !endNode) {
        alert('Debes colocar un inicio y un fin.');
        return;
    }

    clearPath();

    // 👉 Solo guarda el mapa si aún no ha sido guardado
    if (!mapaYaGuardado) {
        await guardarMapaEnBackend();
        mapaYaGuardado = true;
    }

    const inicioX = parseInt(startNode.dataset.x);
    const inicioY = parseInt(startNode.dataset.y);
    const finX = parseInt(endNode.dataset.x);
    const finY = parseInt(endNode.dataset.y);

    try {
        const response = await fetch('/api/ruta', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            body: JSON.stringify({ inicioX, inicioY, finX, finY })
        });

        if (!response.ok) {
            throw new Error('Error en la solicitud al servidor');
        }

        const ruta = await response.json();

        for (let i = 1; i < ruta.length - 1; i++) {
            const { x, y } = ruta[i];
            const cell = document.querySelector(`td[data-x="${x}"][data-y="${y}"]`);
            if (cell) {
                cell.classList.add('path');
                await new Promise(r => setTimeout(r, 30));
            }
        }

    } catch (err) {
        alert('Error: ' + err.message);
    }
}

async function generarMapaBSP() {
    try {
        const response = await fetch('/api/ruta/generar-bsp', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) throw new Error('Error al generar el mapa');

        const mapa = await response.json();

        // ✅ Limpia completamente el grid
        resetGrid();

        // ✅ Limpia todas las clases 'wall' por si quedaron residuos
        document.querySelectorAll('td').forEach(cell => cell.classList.remove('wall'));

        // ✅ Usamos un Set para acceder rápidamente a los nodos válidos
        const nodosSet = new Set(mapa.nodos.map(n => `${n.x},${n.y}`));

        // ✅ Solo marcamos como muro aquellas celdas que no están en nodos
        for (let y = 0; y < rows; y++) {
            for (let x = 0; x < cols; x++) {
                const key = `${x},${y}`;
                const cell = document.querySelector(`td[data-x="${x}"][data-y="${y}"]`);
                if (cell && !nodosSet.has(key)) {
                    cell.classList.add('wall');
                }
            }
        }

        mapaYaGuardado = true; // como vino del backend, ya está guardado

    } catch (err) {
        alert('Error al generar mapa BSP: ' + err.message);
    }
}

createGrid();

async function buscarPorVoz() {
    const recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
    recognition.lang = 'es-ES';
    recognition.interimResults = false;

    recognition.onresult = async (event) => {
        const transcript = event.results[0][0].transcript.trim().toLowerCase();
        const tipoHabitacion = transcript.charAt(0).toUpperCase() + transcript.slice(1);

        console.log("Habitación solicitada por voz:", tipoHabitacion);

        try {
            // Obtener coordenada destino
            const response = await fetch(`/api/ruta/mapa/centro?tipo=${tipoHabitacion}`);
            if (!response.ok) throw new Error("Habitación no encontrada");

            const destino = await response.json();

            if (!startNode) {
                alert("Primero selecciona un nodo de inicio");
                return;
            }

            clearPath();

            if (!mapaYaGuardado) {
                await guardarMapaEnBackend();
                mapaYaGuardado = true;
            }

            const inicioX = parseInt(startNode.dataset.x);
            const inicioY = parseInt(startNode.dataset.y);
            const finX = destino.x;
            const finY = destino.y;

            const rutaResponse = await fetch('/api/ruta', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                },
                body: JSON.stringify({ inicioX, inicioY, finX, finY })
            });

            if (!rutaResponse.ok) throw new Error("No se pudo calcular la ruta");

            const ruta = await rutaResponse.json();

            // Pintar la ruta
            for (let i = 1; i < ruta.length - 1; i++) {
                const { x, y } = ruta[i];
                const cell = document.querySelector(`td[data-x="${x}"][data-y="${y}"]`);
                if (cell) {
                    cell.classList.add('path');
                    await new Promise(r => setTimeout(r, 30));
                }
            }

            // Convertir el nodo destino en nuevo nodo de inicio
            if (endNode) endNode.classList.remove('end');

            const nuevaInicio = document.querySelector(`td[data-x="${finX}"][data-y="${finY}"]`);
            if (nuevaInicio) {
                nuevaInicio.classList.remove('path');
                nuevaInicio.classList.add('start');
                if (startNode) startNode.classList.remove('start');
                startNode = nuevaInicio;
                endNode = null;
                placing = 'wall';
            }

            // Reproducir voz opcional
            const mensaje = `Has llegado al ${tipoHabitacion}`;
            const utterance = new SpeechSynthesisUtterance(mensaje);
            utterance.lang = 'es-ES';
            speechSynthesis.speak(utterance);

        } catch (err) {
            console.error("Error:", err);
            alert("No se pudo calcular la ruta: " + err.message);
        }
    };

    recognition.onerror = (event) => {
        console.error("Error de reconocimiento:", event.error);
        alert("Error al reconocer la voz: " + event.error);
    };

    recognition.start();
}