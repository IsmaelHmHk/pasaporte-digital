-- 1. Insertar Usuarios de prueba
INSERT INTO usuarios (nombre, email, password_hash, rol) VALUES
                                                             ('Juan Pérez (Cliente)', 'cliente@email.com', '$2a$10$HASH_SIMULADO_CLIENTE', 'CLIENTE'),
                                                             ('María López (Técnica)', 'tecnico@email.com', '$2a$10$HASH_SIMULADO_TECNICO', 'TECNICO'),
                                                             ('Admin Sistema', 'admin@pasaporte.com', '$2a$10$HASH_SIMULADO_ADMIN', 'ADMIN');
-- 2. Insertar Componentes de Hardware
INSERT INTO componentes (num_serie, modelo, fabricante, tipo_componente, estado, usuario_id) VALUES
                                                                                                 ('SN-GPU-4090-001', 'GeForce RTX 4090', 'Asus', 'Tarjeta Gráfica', 'ACTIVO', 1),
                                                                                                 ('SN-CPU-13900K-002', 'Core i9-13900K', 'Intel', 'Procesador', 'ACTIVO', 1),
                                                                                                 ('SN-RAM-32GB-003', 'Vengeance DDR5 32GB', 'Corsair', 'Memoria RAM', 'EN_REPARACION', 2);
-- Insertar Garantías
INSERT INTO garantias (componente_id, fecha_inicio, fecha_fin, tipo_garantia, estado) VALUES
                                                                                          (1, '2024-01-15', '2027-01-15', 'FABRICANTE_DIRECTA', 'ACTIVA'),
                                                                                          (2, '2024-02-10', '2026-02-10', 'ESTANDAR_B2B', 'ACTIVA');

-- Insertar Registros de Trazabilidad en el Historial
INSERT INTO historial_eventos (componente_id, usuario_id, tipo_evento, descripcion) VALUES
                                                                                        (1, 1, 'FABRICACION', 'Componente ensamblado y verificado en planta de producción.'),
                                                                                        (1, 1, 'VENTA', 'Componente distribuido y registrado a nombre del cliente.'),
-- Insertar Solicitudes de RMA
INSERT INTO solicitudes_rma (componente_id, usuario_id, codigo_rma, motivo, estado) VALUES
    (3, 2, 'RMA-2026-001', 'Fallo en módulos de memoria tras prueba de carga prolongada.', 'PENDIENTE');                                                                                        (3, 2, 'INSPECCION', 'Ingreso en taller técnico por informe de sobrecalentamiento.');