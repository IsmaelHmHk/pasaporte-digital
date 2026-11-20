import { useEffect, useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { apiFetch } from '../services/api';
import type { PasaporteDTO } from '../types/pasaporte';

export function VerificadorPage() {
    const { numSerie } = useParams<{ numSerie: string }>();
    const navigate = useNavigate();

    const [busqueda, setBusqueda] = useState(numSerie || '');
    const [pasaporte, setPasaporte] = useState<PasaporteDTO | null>(null);
    const [cargando, setCargando] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!numSerie) {
            setPasaporte(null);
            setError('');
            return;
        }

        setBusqueda(numSerie);
        setCargando(true);
        setError('');

        apiFetch<PasaporteDTO>(`/pasaporte/${encodeURIComponent(numSerie)}`)
            .then((data) => setPasaporte(data))
            .catch(() => {
                setPasaporte(null);
                setError(`No se encontró ningún componente registrado con el número de serie "${numSerie}".`);
            })
            .finally(() => setCargando(false));
    }, [numSerie]);

    const handleBuscar = (e: React.FormEvent) => {
        e.preventDefault();
        if (busqueda.trim()) {
            navigate(`/pasaporte/${encodeURIComponent(busqueda.trim())}`);
        }
    };

    const formatearFecha = (fechaIso?: string) => {
        if (!fechaIso) return 'No disponible';
        return new Date(fechaIso).toLocaleDateString('es-ES', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
        });
    };

    return (
        <div className="contenedor">
            <header className="barra-superior">
                <div>
                    <h1>Pasaporte Digital de Hardware</h1>
                    <p className="subtitulo">Sistema de trazabilidad y verificación de garantía</p>
                </div>
                <Link to="/login" className="btn-secundario">
                    Acceso Usuarios / Técnicos
                </Link>
            </header>

            <section className="tarjeta">
                <form onSubmit={handleBuscar} className="buscador-form">
                    <input
                        type="text"
                        placeholder="Introduce un número de serie (ej. SN-GPU-4090-001)"
                        value={busqueda}
                        onChange={(e) => setBusqueda(e.target.value)}
                    />
                    <button type="submit" className="btn-primario">
                        Verificar Hardware
                    </button>
                </form>
            </section>

            {cargando && <p className="mensaje-info">Consultando pasaporte en servidor...</p>}
            {error && <div className="alerta-error">{error}</div>}

            {pasaporte && !cargando && (
                <section className="tarjeta ficha-pasaporte">
                    <div className="ficha-cabecera">
                        <div>
                            <span className="etiqueta-tipo">{pasaporte.tipoComponente}</span>
                            <h2>{pasaporte.fabricante} {pasaporte.modelo}</h2>
                            <p className="num-serie">S/N: <strong>{pasaporte.numSerie}</strong></p>
                        </div>

                        <div className={`insignia-garantia ${pasaporte.enGarantia ? 'activa' : 'expirada'}`}>
                            {pasaporte.enGarantia ? 'GARANTÍA ACTIVA' : 'GARANTÍA EXPIRADA'}
                        </div>
                    </div>

                    <div className="grid-datos">
                        <div className="dato-item">
                            <span>Estado Operativo</span>
                            <strong>{pasaporte.estado}</strong>
                        </div>
                        <div className="dato-item">
                            <span>Fecha de Registro</span>
                            <strong>{formatearFecha(pasaporte.fechaRegistro)}</strong>
                        </div>
                        <div className="dato-item">
                            <span>Cobertura Hasta</span>
                            <strong>{formatearFecha(pasaporte.fechaFinGarantia)}</strong>
                        </div>
                    </div>
                </section>
            )}
        </div>
    );
}