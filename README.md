# Pasaporte Digital de Hardware y Gestión de RMA (SAP B2B)

## Descripción del Proyecto
Plataforma para la gestión de trazabilidad de componentes de hardware y procesamiento de garantías/RMA. La solución permite consultar la hoja de vida (Pasaporte Digital) de un componente mediante escaneo de código QR/barras o NFC desde una aplicación móvil, verificar el estado de su garantía en tiempo real y tramitar solicitudes de sustitución o reparación integradas con un entorno SAP.

## Stack Tecnológico
- **Backend:** Java (Spring Boot), API REST, Spring Data JPA.
- **Frontend Móvil:** Android (Kotlin, Jetpack Compose), CameraX / ML Kit.
- **Base de Datos:** PostgreSQL.
- **Integraciones:** Simulación de API OData para módulos SAP (MM/SD).
- **Herramientas & Despliegue:** Docker, Git/GitHub, Postman, DBeaver.

##  Estructura del Repositorio
- `/backend`: Proyecto Spring Boot.
- `/android`: Aplicación móvil en Jetpack Compose.
- `/database`: Scripts de creación de esquemas y datos de prueba en PostgreSQL.
- `/docs`: Documentación técnica y memoria del TFG.