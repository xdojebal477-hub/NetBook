# 📚 NetBook - Tu Biblioteca Digital Interactiva

**Proyecto Integrado - 2º DAW (Desarrollo de Aplicaciones Web)**  
**Autor:** Daniel Ojeda Balsera  
**Centro:** IES Hermanos Machado (Dos Hermanas, Sevilla)  
**Curso:** 2025/2026

![NetBook Banner](https://img.shields.io/badge/NetBook-Biblioteca_Interactiva-indigo?style=for-the-badge)

NetBook es una plataforma moderna, escalable y responsive diseñada para transformar la experiencia de lectura digital. Conecta a lectores y autores independientes en un único espacio donde descubrir obras, debatir en vivo y gestionar catálogos literarios de manera eficiente.

---

## 🚀 Tecnologías y Arquitectura (Desacoplada)

El proyecto utiliza una arquitectura Cliente-Servidor separada en dos repositorios/carpetas:

### Frontend (Capa de Presentación)
*   **Framework:** Angular 17+ (TypeScript)
*   **Diseño:** Tailwind CSS (Mobile-first, diseño fluido y responsivo)
*   **Despliegue:** [Vercel](https://vercel.com)

### Backend (Capa de Negocio y Lógica)
*   **Framework:** Spring Boot 3 (Java)
*   **Seguridad:** Spring Security + JWT (JSON Web Tokens) Stateless
*   **Comunicaciones Real-Time:** Server-Sent Events (SSE) para salas de chat
*   **Despliegue:** [Railway](https://railway.app) (Docker Container + Volúmenes Persistentes para Storage de PDFs/Imágenes)
*   **BBDD:** MySQL  en Railway

---

## 🔑 Credenciales de Acceso (Cumplimiento Anexo II - LEEME.TXT)

Para que el tribunal y los evaluadores puedan probar todas las funcionalidades e interfaces del sistema sin necesidad de registrar nuevos usuarios, se proporcionan los siguientes perfiles preconfigurados:

| Rol | Correo Electrónico (Usuario) | Contraseña | Funcionalidad a probar |
| :--- | :--- | :--- | :--- |
| **Administrador** | `admin@netbook.com` | `Admin12345` | - Moderar reseñas<br>- Eliminar libros infractores<br>- Supervisión total |
| **Autor** | `autor@netbook.com` | `autor123` | - Subir nuevos libros (PDF)<br>- Editar sinopsis, portada y metadatos<br>- Visualizar "Mis Obras" |
| **Lector** | `lector@netbook.com` | `lector123` | - Búsqueda en catálogo<br>- Favoritos / Colecciones<br>- Lectura en Visor Web<br>- Valorar y reseñar obras<br>- Unirse a Comunidades (Chat SSE) |

*(Nota para el tribunal: Sustituir las contraseñas arriba indicadas si se han modificado durante el volcado de la base de datos de producción).*

---

## 🌍 Enlaces de Despliegue en Producción

*   **Aplicación Web (Vercel):** `https://net-book-rust.vercel.app/`
*   **API / Backend (Railway):** `https://netbook-backend-production.up.railway.app`

*(El sistema de archivos de libros como `.pdf` y avatares se encuentra alojado persistentemente en la estructura montada de Railway)*.

---

## ⚙️ Instrucciones de Despliegue Local (Para Desarrollo)

Si deseas ejecutar NetBook en una máquina local:

### 1. Clonar el repositorio
```bash
git clone https://github.com/xdojebal477-hub/NetBook.git
cd NetBook
```

### 2. Arrancar el Backend (Spring Boot)
1. Instalar Java JDK 17 o superior.
2. Poseer un servidor SQL local corriendo la base de datos configurada en `application.properties`.
3. Navegar a la carpeta backend:
```bash
cd backend
./mvnw spring-boot:run
```
*El backend arrancará expuesto en el puerto definido (ej: `http://localhost:8081`).*

### 3. Arrancar el Frontend (Angular)
1. Instalar Node.js v18+.
2. Navegar a la carpeta frontend:
```bash
cd frontend/frontend
npm install
npm start
```
*El frontend se expondrá típicamente en `http://localhost:4200` y estará configurado para apuntar a la API local/remota según el `environment`.*

