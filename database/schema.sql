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
                             numero_serie VARCHAR(100) UNIQUE NOT NULL,
                             modelo VARCHAR(100) NOT NULL,
                             marca VARCHAR(100) NOT NULL,
                             categoria VARCHAR(50) NOT NULL,
                             fecha_fabricacion DATE NOT NULL
);

-- 3. Tabla Garantías (Asociación componente-cliente)
CREATE TABLE garantias (
                           id BIGSERIAL PRIMARY KEY,
                           componente_id BIGINT NOT NULL REFERENCES componentes(id) ON DELETE CASCADE,
                           cliente_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                           fecha_compra DATE NOT NULL,
                           periodo_meses INT NOT NULL CHECK (periodo_meses > 0)
);

-- 4. Tabla Historial de Eventos (Pasaporte Digital / Trazabilidad)
CREATE TABLE historial_eventos (
                                   id BIGSERIAL PRIMARY KEY,
                                   componente_id BIGINT NOT NULL REFERENCES componentes(id) ON DELETE CASCADE,
                                   tipo_evento VARCHAR(30) NOT NULL CHECK (tipo_evento IN ('FABRICACION', 'VENTA', 'REPARACION', 'SUSTITUCION', 'INSPECCION')),
                                   descripcion TEXT NOT NULL,
                                   fecha_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   registrado_por BIGINT NOT NULL REFERENCES usuarios(id)
);

-- 5. Tabla Solicitudes RMA (Gestión de averías e integración SAP)
CREATE TABLE solicitudes_rma (
                                 id BIGSERIAL PRIMARY KEY,
                                 garantia_id BIGINT NOT NULL REFERENCES garantias(id) ON DELETE CASCADE,
                                 tecnico_id BIGINT REFERENCES usuarios(id),
                                 fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 motivo_averia TEXT NOT NULL,
                                 estado_tramite VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' CHECK (estado_tramite IN ('PENDIENTE', 'EN_REVISION', 'APROBADO', 'RECHAZADO', 'RESUELTO')),
                                 codigo_seguimiento_sap VARCHAR(50)
);