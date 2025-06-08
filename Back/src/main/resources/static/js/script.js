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
            headers: { 'Content-Type': 'application/json' },
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

createGrid();