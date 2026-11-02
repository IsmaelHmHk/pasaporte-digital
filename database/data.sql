-- 1. Insertar Usuarios de prueba
INSERT INTO usuarios (nombre, email, password_hash, rol) VALUES
                                                             ('Juan Pérez (Cliente)', 'cliente@email.com', '$2a$10$HASH_SIMULADO_CLIENTE', 'CLIENTE'),
                                                             ('María López (Técnica)', 'tecnico@email.com', '$2a$10$HASH_SIMULADO_TECNICO', 'TECNICO'),
                                                             ('Admin Sistema', 'admin@pasaporte.com', '$2a$10$HASH_SIMULADO_ADMIN', 'ADMIN');

-- 2. Insertar Componentes de Hardware
INSERT INTO componentes (numero_serie, modelo, marca, categoria, fecha_fabricacion) VALUES
                                                                                        ('SN-GPU-4090-001', 'GeForce RTX 4090', 'Asus', 'Tarjeta Gráfica', '2024-01-15'),
                                                                                        ('SN-CPU-13900K-002', 'Core i9-13900K', 'Intel', 'Procesador', '2023-11-20'),
                                                                                        ('SN-RAM-32GB-003', 'Vengeance DDR5 32GB', 'Corsair', 'Memoria RAM', '2024-03-10');

-- 3. Insertar Garantías asociadas
INSERT INTO garantias (componente_id, cliente_id, fecha_compra, periodo_meses) VALUES
                                                                                   (1, 1, '2024-02-01', 36), -- Garantía activa de 3 años
                                                                                   (2, 1, '2022-01-10', 24); -- Garantía de 2 años ya caducada (para pruebas)

-- 4. Insertar Historial de Eventos (Pasaporte Digital)
INSERT INTO historial_eventos (componente_id, tipo_evento, descripcion, registrado_por) VALUES
                                                                                            (1, 'FABRICACION', 'Componente ensamblado y superado el control de calidad en fábrica.', 3),
                                                                                            (1, 'VENTA', 'Componente vendido y registrado a nombre del cliente.', 3);