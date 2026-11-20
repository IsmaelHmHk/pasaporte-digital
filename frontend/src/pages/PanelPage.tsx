import { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { apiFetch, apiFetchBlob, getToken, removeToken } from '../services/api';
import type { Componente, SolicitudRmaDTO, UsuarioPerfil } from '../types/pasaporte';

export function PanelPage() {
  const navigate = useNavigate();

  const [usuario, setUsuario] = useState<UsuarioPerfil | null>(null);
  const [componentes, setComponentes] = useState<Componente[]>([]);
  const [rmas, setRmas] = useState<SolicitudRmaDTO[]>([]);
  const [pestana, setPestana] = useState<'componentes' | 'rmas'>('componentes');

  const [cargando, setCargando] = useState(true);
  const [mensaje, setMensaje] = useState('');
  const [error, setError] = useState('');

  // Modal QR
  const [qrModal, setQrModal] = useState<{ numSerie: string; url: string } | null>(null);

  // Formulario nuevo componente (ADMIN / TECNICO)
  const [numSerie, setNumSerie] = useState('');
  const [modelo, setModelo] = useState('');
  const [fabricante, setFabricante] = useState('');
  const [tipoComponente, setTipoComponente] = useState('GPU');

  // Formulario nuevo RMA
  const [rmaNumSerie, setRmaNumSerie] = useState('');
  const [rmaMotivo, setRmaMotivo] = useState('');

  const esTecnicoOAdmin = usuario?.rol === 'TECNICO' || usuario?.rol === 'ADMIN';

  const cargarDatos = async () => {
    try {
      const [perfilData, compData, rmaData] = await Promise.all([
        apiFetch<UsuarioPerfil>('/auth/me'),
        apiFetch<Componente[]>('/componentes'),
        apiFetch<SolicitudRmaDTO[]>('/rmas'),
      ]);
      setUsuario(perfilData);
      setComponentes(compData);
      setRmas(rmaData);
      if (compData.length > 0 && !rmaNumSerie) {
        setRmaNumSerie(compData[0].numSerie);
      }
    } catch {
      removeToken();
      navigate('/login');
    } finally {
      setCargando(false);
    }
  };

  useEffect(() => {
    if (!getToken()) {
      navigate('/login');
      return;
    }
    cargarDatos();
  }, []);

  const cerrarSesion = () => {
    removeToken();
    navigate('/login');
  };

  const verQr = async (serie: string) => {
    setError('');
    try {
      const blob = await apiFetchBlob(`/componentes/${encodeURIComponent(serie)}/qr`);
      const url = URL.createObjectURL(blob);
      setQrModal({ numSerie: serie, url });
    } catch {
      setError('No se pudo cargar el código QR del componente.');
    }
  };

  const cerrarQrModal = () => {
    if (qrModal) {
      URL.revokeObjectURL(qrModal.url);
      setQrModal(null);
    }
  };

  const descargarPdf = async (serie: string) => {
    setError('');
    try {
      const blob = await apiFetchBlob(`/componentes/${encodeURIComponent(serie)}/pdf`);
      const url = URL.createObjectURL(blob);
      const enlace = document.createElement('a');
      enlace.href = url;
      enlace.download = `pasaporte-${serie}.pdf`;
      document.body.appendChild(enlace);
      enlace.click();
      enlace.remove();
      URL.revokeObjectURL(url);
    } catch {
      setError('No se pudo descargar el certificado PDF.');
    }
  };

  const handleCrearComponente = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setMensaje('');
    try {
      await apiFetch('/componentes', {
        method: 'POST',
        body: JSON.stringify({
          numSerie: numSerie.trim(),
          modelo: modelo.trim(),
          fabricante: fabricante.trim(),
          tipoComponente,
          estado: 'OPERATIVO',
        }),
      });
      setNumSerie('');
      setModelo('');
      setFabricante('');
      setMensaje('Componente registrado correctamente en el pasaporte digital.');
      await cargarDatos();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al registrar el componente.');
    }
  };

  const handleCrearRma = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setMensaje('');
    try {
      await apiFetch('/rmas', {
        method: 'POST',
        body: JSON.stringify({
          numSerie: rmaNumSerie,
          motivo: rmaMotivo.trim(),
        }),
      });
      setRmaMotivo('');
      setMensaje('Solicitud de RMA creada correctamente.');
      await cargarDatos();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo abrir la solicitud RMA.');
    }
  };

  const handleCambiarEstadoRma = async (idRma: number, nuevoEstado: string) => {
    setError('');
    setMensaje('');
    try {
      await apiFetch(`/rmas/${idRma}/estado`, {
        method: 'PATCH',
        body: JSON.stringify({ estado: nuevoEstado }),
      });
      setMensaje(`Incidencia actualizada a estado ${nuevoEstado}.`);
      await cargarDatos();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al actualizar el estado.');
    }
  };

  if (cargando) {
    return (
      <div className="contenedor">
        <p className="mensaje-info">Cargando panel de control...</p>
      </div>
    );
  }

  return (
    <div className="contenedor contenedor-ancho">
      <header className="barra-superior">
        <div>
          <h1>Panel de Gestión</h1>
          <p className="subtitulo">
            Sesión iniciada como <strong>{usuario?.nombre}</strong> ({usuario?.email}) — Rol:{' '}
            <span className="badge-rol">{usuario?.rol}</span>
          </p>
        </div>

        <div className="acciones-header">
          <Link to="/" className="btn-secundario">
            Verificador Público
          </Link>
          <button type="button" onClick={cerrarSesion} className="btn-peligro">
            Cerrar Sesión
          </button>
        </div>
      </header>

      <div className="tabs-panel">
        <button
          type="button"
          className={pestana === 'componentes' ? 'tab-activa' : ''}
          onClick={() => {
            setPestana('componentes');
            setError('');
            setMensaje('');
          }}
        >
          Catálogo de Hardware ({componentes.length})
        </button>
        <button
          type="button"
          className={pestana === 'rmas' ? 'tab-activa' : ''}
          onClick={() => {
            setPestana('rmas');
            setError('');
            setMensaje('');
          }}
        >
          Incidencias RMA ({rmas.length})
        </button>
      </div>

      {error && <div className="alerta-error margen-abajo">{error}</div>}
      {mensaje && <div className="alerta-ok margen-abajo">{mensaje}</div>}

      {pestana === 'componentes' && (
        <>
          {esTecnicoOAdmin && (
            <section className="tarjeta">
              <h2 className="titulo-seccion">Registrar Nuevo Componente</h2>
              <form onSubmit={handleCrearComponente} className="grid-form">
                <input
                  type="text"
                  required
                  placeholder="Nº Serie (ej. SN-CPU-7800X3D)"
                  value={numSerie}
                  onChange={(e) => setNumSerie(e.target.value)}
                />
                <input
                  type="text"
                  required
                  placeholder="Fabricante (ej. AMD)"
                  value={fabricante}
                  onChange={(e) => setFabricante(e.target.value)}
                />
                <input
                  type="text"
                  required
                  placeholder="Modelo (ej. Ryzen 7 7800X3D)"
                  value={modelo}
                  onChange={(e) => setModelo(e.target.value)}
                />
                <select
                  value={tipoComponente}
                  onChange={(e) => setTipoComponente(e.target.value)}
                >
                  <option value="GPU">GPU</option>
                  <option value="CPU">CPU</option>
                  <option value="PLACA_BASE">Placa Base</option>
                  <option value="RAM">Memoria RAM</option>
                  <option value="ALMACENAMIENTO">Almacenamiento</option>
                  <option value="FUENTE">Fuente Alimentación</option>
                </select>
                <button type="submit" className="btn-primario">
                  + Añadir Hardware
                </button>
              </form>
            </section>
          )}

          <section className="tarjeta tabla-responsive">
            <h2 className="titulo-seccion">Inventario Registrado</h2>
            <table className="tabla-datos">
              <thead>
                <tr>
                  <th>Nº Serie</th>
                  <th>Componente</th>
                  <th>Tipo</th>
                  <th>Estado</th>
                  <th>Pasaporte Digital</th>
                </tr>
              </thead>
              <tbody>
                {componentes.map((comp) => (
                  <tr key={comp.id}>
                    <td>
                      <Link to={`/pasaporte/${encodeURIComponent(comp.numSerie)}`} className="enlace-tabla">
                        {comp.numSerie}
                      </Link>
                    </td>
                    <td>
                      <strong>{comp.fabricante}</strong> {comp.modelo}
                    </td>
                    <td>{comp.tipoComponente}</td>
                    <td>
                      <span className={`estado-pill ${comp.estado === 'OPERATIVO' ? 'ok' : 'warn'}`}>
                        {comp.estado}
                      </span>
                    </td>
                    <td className="celda-acciones">
                      <button
                        type="button"
                        className="btn-mini"
                        onClick={() => verQr(comp.numSerie)}
                      >
                        Ver QR
                      </button>
                      <button
                        type="button"
                        className="btn-mini btn-mini-azul"
                        onClick={() => descargarPdf(comp.numSerie)}
                      >
                        Descargar PDF
                      </button>
                    </td>
                  </tr>
                ))}
                {componentes.length === 0 && (
                  <tr>
                    <td colSpan={5} className="texto-centro">
                      No hay componentes registrados todavía.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </section>
        </>
      )}

      {pestana === 'rmas' && (
        <>
          <section className="tarjeta">
            <h2 className="titulo-seccion">Abrir Nueva Solicitud de Reparación (RMA)</h2>
            <form onSubmit={handleCrearRma} className="grid-form-rma">
              <select
                value={rmaNumSerie}
                onChange={(e) => setRmaNumSerie(e.target.value)}
                required
              >
                <option value="" disabled>
                  Selecciona un componente...
                </option>
                {componentes.map((c) => (
                  <option key={c.id} value={c.numSerie}>
                    {c.numSerie} — {c.fabricante} {c.modelo} ({c.estado})
                  </option>
                ))}
              </select>

              <input
                type="text"
                required
                placeholder="Describe el fallo técnico detectado..."
                value={rmaMotivo}
                onChange={(e) => setRmaMotivo(e.target.value)}
              />

              <button type="submit" className="btn-primario">
                Solicitar RMA
              </button>
            </form>
          </section>

          <section className="tarjeta tabla-responsive">
            <h2 className="titulo-seccion">
              {esTecnicoOAdmin ? 'Todas las Solicitudes RMA' : 'Mis Solicitudes RMA'}
            </h2>
            <table className="tabla-datos">
              <thead>
                <tr>
                  <th>Código RMA</th>
                  <th>Hardware</th>
                  <th>Solicitante</th>
                  <th>Motivo</th>
                  <th>Estado</th>
                  {esTecnicoOAdmin && <th>Resolución Técnica</th>}
                </tr>
              </thead>
              <tbody>
                {rmas.map((rma) => (
                  <tr key={rma.id}>
                    <td><strong>{rma.codigoRma}</strong></td>
                    <td>
                      {rma.modeloComponente}
                      <div className="subtexto-tabla">{rma.numSerieComponente}</div>
                    </td>
                    <td>{rma.emailUsuario}</td>
                    <td>{rma.motivo}</td>
                    <td>
                      <span className="estado-pill warn">{rma.estado}</span>
                    </td>
                    {esTecnicoOAdmin && (
                      <td className="celda-acciones">
                        <button
                          type="button"
                          className="btn-mini"
                          onClick={() => handleCambiarEstadoRma(rma.id, 'EN_REVISION')}
                        >
                          En Revisión
                        </button>
                        <button
                          type="button"
                          className="btn-mini btn-mini-verde"
                          onClick={() => handleCambiarEstadoRma(rma.id, 'REPARADO')}
                        >
                          Reparado
                        </button>
                        <button
                          type="button"
                          className="btn-mini btn-mini-rojo"
                          onClick={() => handleCambiarEstadoRma(rma.id, 'RECHAZADO')}
                        >
                          Rechazar
                        </button>
                      </td>
                    )}
                  </tr>
                ))}
                {rmas.length === 0 && (
                  <tr>
                    <td colSpan={esTecnicoOAdmin ? 6 : 5} className="texto-centro">
                      No hay solicitudes de RMA registradas.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </section>
        </>
      )}

      {qrModal && (
        <div className="modal-fondo" onClick={cerrarQrModal}>
          <div className="modal-caja" onClick={(e) => e.stopPropagation()}>
            <h3>Código QR — {qrModal.numSerie}</h3>
            <p className="subtitulo">Apunta a /api/pasaporte/{qrModal.numSerie}</p>
            <img src={qrModal.url} alt={`QR ${qrModal.numSerie}`} className="imagen-qr" />
            <button type="button" className="btn-primario btn-ancho" onClick={cerrarQrModal}>
              Cerrar
            </button>
          </div>
        </div>
      )}
    </div>
  );
}