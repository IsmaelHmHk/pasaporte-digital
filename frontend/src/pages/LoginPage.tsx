import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { apiFetch, setToken } from '../services/api';

export function LoginPage() {
  const navigate = useNavigate();
  const [modoRegistro, setModoRegistro] = useState(false);

  const [nombre, setNombre] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState('');
  const [mensajeOk, setMensajeOk] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setMensajeOk('');
    setCargando(true);

    try {
      if (modoRegistro) {
        await apiFetch('/auth/registro', {
          method: 'POST',
          body: JSON.stringify({ nombre, email, password }),
        });
        setMensajeOk('Cuenta creada correctamente. Ya puedes iniciar sesión.');
        setModoRegistro(false);
        setPassword('');
      } else {
        const respuesta = await apiFetch<{ token?: string } | string>('/auth/login', {
          method: 'POST',
          body: JSON.stringify({ email, password }),
        });

        const tokenRecibido =
          typeof respuesta === 'string' ? respuesta : respuesta.token;

        if (!tokenRecibido) {
          throw new Error('El servidor no devolvió un token válido.');
        }

        setToken(tokenRecibido);
        navigate('/panel');
      }
    } catch (err) {
      const detalle = err instanceof Error ? err.message : 'Credenciales incorrectas';
      setError(detalle);
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="contenedor contenedor-estrecho">
      <header className="barra-superior">
        <div>
          <h1>{modoRegistro ? 'Crear Cuenta' : 'Iniciar Sesión'}</h1>
          <p className="subtitulo">Acceso al inventario y gestión de garantías RMA</p>
        </div>
        <Link to="/" className="btn-secundario">
          Volver al Verificador
        </Link>
      </header>

      <section className="tarjeta">
        <div className="tabs-auth">
          <button
            type="button"
            className={!modoRegistro ? 'tab-activa' : ''}
            onClick={() => {
              setModoRegistro(false);
              setError('');
            }}
          >
            Entrar
          </button>
          <button
            type="button"
            className={modoRegistro ? 'tab-activa' : ''}
            onClick={() => {
              setModoRegistro(true);
              setError('');
            }}
          >
            Registrarse
          </button>
        </div>

        <form onSubmit={handleSubmit} className="form-vertical">
          {modoRegistro && (
            <div className="campo-form">
              <label>Nombre completo</label>
              <input
                type="text"
                required
                placeholder="Ej. Ismael"
                value={nombre}
                onChange={(e) => setNombre(e.target.value)}
              />
            </div>
          )}

          <div className="campo-form">
            <label>Correo electrónico</label>
            <input
              type="email"
              required
              placeholder="usuario@ejemplo.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div className="campo-form">
            <label>Contraseña</label>
            <input
              type="password"
              required
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          {error && <div className="alerta-error">{error}</div>}
          {mensajeOk && <div className="alerta-ok">{mensajeOk}</div>}

          <button type="submit" className="btn-primario btn-ancho" disabled={cargando}>
            {cargando
              ? 'Procesando...'
              : modoRegistro
                ? 'Registrar Usuario'
                : 'Acceder al Panel'}
          </button>
        </form>
      </section>
    </div>
  );
}