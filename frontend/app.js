const api = 'http://localhost:8080/';
const $ = id => document.getElementById(id);

async function get(path) {
    const r = await fetch(api + path);
    if (!r.ok) throw new Error('No se pudo cargar la información');
    return r.json()
}

async function cargarAulas() {
    try {
        const p = new URLSearchParams();
        if ($('capacidad').value) p.set('capacidad', $('capacidad').value);
        if ($('ordenadores').checked) p.set('ordenadores', 'true');
        const aulas = await get('aulas?' + p);
        $('estado').textContent = `${aulas.length} aula(s) encontrada(s)`;
        $('aulas').innerHTML = aulas.map(a => `<article class="room"><strong>${a.nombre}</strong><br>Capacidad: ${a.capacidad}<br>${a.ordenadores ? '💻 Con ordenadores' : 'Sin ordenadores'}</article>`).join('');
        $('aula').innerHTML = aulas.map(a => `<option value="${a.id}">${a.nombre}</option>`).join('')
    } catch (e) {
        $('estado').textContent = e.message
    }
}

async function cargarSelects() {
    try {
        const [ps, fs] = await Promise.all([get('profesores'), get('franjas')]);
        $('profesor').innerHTML = ps.map(p => `<option value="${p.id}">${p.nombre}</option>`).join('');
        $('franja').innerHTML = fs.map(f => `<option value="${f.id}">${f.horaInicio} - ${f.horaFin}</option>`).join('')
    } catch (e) {
        $('mensaje').textContent = e.message;
        $('mensaje').className = 'error'
    }
}

$('filtro').addEventListener('submit', e => {
    e.preventDefault();
    cargarAulas()
});
$('reserva').addEventListener('submit', async e => {
    e.preventDefault();
    const body = {
        fechaReserva: $('fecha').value,
        aula: {id: +$('aula').value, ordenadores: false},
        profesor: {id: +$('profesor').value},
        franja: {id: +$('franja').value}
    };
    try {
        const r = await fetch(api + 'reservas', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        const d = await r.json();
        if (!r.ok) throw new Error(d.message || 'No se pudo crear la reserva');
        $('mensaje').textContent = 'Reserva creada correctamente';
        $('mensaje').className = 'ok'
    } catch (x) {
        $('mensaje').textContent = x.message;
        $('mensaje').className = 'error'
    }
});
cargarAulas();
cargarSelects();
