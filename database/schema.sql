-- Eliminación de tablas si ya existen (para poder reiniciar el esquema limpiamente)
DROP TABLE IF EXISTS solicitudes_rma;
DROP TABLE IF EXISTS historial_eventos;
DROP TABLE IF EXISTS garantias;
DROP TABLE IF EXISTS componentes;
DROP TABLE IF EXISTS usuarios;

-- 1. Tabla Usuarios (Gestión de acceso y roles)
CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(150) UNIQUE NOT NULL,
                          password_hash VARCHAR(255) NOT NULL,
                          rol VARCHAR(20) NOT NULL CHECK (rol IN ('CLIENTE', 'TECNICO', 'ADMIN')),
                          fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla Componentes (Datos físicos de hardware)
CREATE TABLE componentes (
                             id BIGSERIAL PRIMARY KEY,
                             num_serie VARCHAR(100) UNIQUE NOT NULL,
                             modelo VARCHAR(100) NOT NULL,
                             fabricante VARCHAR(100) NOT NULL,
                             tipo_componente VARCHAR(50) NOT NULL,
                             estado VARCHAR(50) NOT NULL,
                             usuario_id BIGINT REFERENCES usuarios(id) ON DELETE SET NULL,
                             fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla Garantías
CREATE TABLE garantias (
                           id BIGSERIAL PRIMARY KEY,
                           componente_id BIGINT NOT NULL UNIQUE REFERENCES componentes(id) ON DELETE CASCADE,
                           fecha_inicio DATE NOT NULL,
                           fecha_fin DATE NOT NULL,
                           tipo_garantia VARCHAR(50) NOT NULL,
                           estado VARCHAR(50) NOT NULL
);

-- 4. Tabla Historial de Eventos (Pasaporte Digital / Trazabilidad)
CREATE TABLE historial_eventos (
                                   id BIGSERIAL PRIMARY KEY,
                                   componente_id BIGINT NOT NULL REFERENCES componentes(id) ON DELETE CASCADE,
                                   usuario_id BIGINT REFERENCES usuarios(id) ON DELETE SET NULL,
                                   tipo_evento VARCHAR(50) NOT NULL,
                                   descripcion TEXT NOT NULL,
                                   fecha_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Tabla Solicitudes de RMA (Gestión de devoluciones y reparaciones B2B)
CREATE TABLE solicitudes_rma (
                                 id BIGSERIAL PRIMARY KEY,
                                 componente_id BIGINT NOT NULL REFERENCES componentes(id) ON DELETE CASCADE,
                                 usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                                 codigo_rma VARCHAR(50) NOT NULL UNIQUE,
                                 motivo TEXT NOT NULL,
                                 estado VARCHAR(50) NOT NULL,
                                 fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);