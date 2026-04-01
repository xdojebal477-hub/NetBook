# **PROYECTO INTEGRADO: NETBOOK**

**Autor:** Daniel Ojeda Balsera

**Curso:** 2025/2026

**Titulación:** TÉCNICO SUPERIOR DE DESARROLLO DE APLICACIONES WEB

**Centro:** IES HERMANOS MACHADO, DOS HERMANAS (SEVILLA)

## **Índice**

1. Descripción del problema  
2. Funcionalidades principales  
3. Estudio de mercado y Diferenciación  
4. Objetivos (Generales y Específicos)  
5. Requisitos (Funcionales, No Funcionales, Opcionales)  
6. Matriz de Trazabilidad  
7. Casos de Uso  
8. Diagrama de Clases y Bocetos de Interfaz  
9. Modelo Relacional de Base de Datos  
10. Arquitectura de Software y Stack Tecnológico  
11. Planificación y Cronograma de Desarrollo

## **1\. Descripción del problema**

Hoy en día existe un exceso de información y de libros disponibles en múltiples formatos (físico, digital, plataformas de lectura online). Sin embargo, los usuarios se enfrentan a dos grandes problemas:

* Dificultad para descubrir libros relevantes entre un gran catálogo de ejemplares.  
* Falta de personalización real en las recomendaciones de lectura.  
* Limitada interacción social entre lectores (reseñas dispersas, comunidades poco estructuradas y poco conectadas entre sí).

Por otro lado, los autores independientes no tienen espacios accesibles para publicar, distribuir y recibir feedback sobre sus obras sin depender de grandes editoriales o de las redes sociales, las cuales están acaparadas.

Mi solución propuesta es una Biblioteca Digital Inteligente, NetBook, que permite a los usuarios gestionar libros (leer, valorar, reseñar, organizar en colecciones) y, gracias a la IA, recibir recomendaciones personalizadas y análisis de reseñas. Además, contará con un espacio para que autores publiquen directamente sus obras.

## **2\. Funcionalidades principales**

### **Para lectores:**

* Registro/login.  
* Búsqueda y lectura de libros digitales (texto).  
* Valoración y reseña de libros.  
* Creación de colecciones personales ("favoritos", "para más tarde", "filtrado por tema").  
* Sistema de recomendación por IA de lecturas.  
* Sistema de recomendación por IA para encontrar lectores con gustos similares.  
* Análisis de reseñas para descubrir qué libros gustan más.  
* Creación de comunidades con más lectores según temas de interés.

### **Para autores:**

* Subida de libros (PDF o texto).  
* Gestión de sus obras (editar, eliminar, estadísticas de lecturas y valoraciones).  
* Poder crear comunidades con lectores.

### **Para administrador:**

* Supervisión del sistema: número de libros, usuarios, valoraciones.  
* Garantizar el correcto funcionamiento de la plataforma.

### **Extensiones opcionales:**

* Chatbot para responder dudas sobre libros/autores.  
* OCR para digitalizar libros desde imágenes.

## **3\. Estudio de mercado y Diferenciación**

### **Estudio de mercado**

Existen plataformas con funcionalidades similares:

* **Goodreads (Amazon):** red social de lectores con reseñas y listas.  
* **Wattpad:** autores pueden publicar relatos y novelas, interacción con lectores.  
* **Kindle (Amazon):** plataforma de lectura con recomendaciones, pero centrada en compras.  
* **Google Books:** catálogo de obras, algunas de dominio público.

### **Diferenciación**

* A diferencia de Goodreads, NetBook integra un sistema de recomendación personalizado con IA, inspirado en Netflix, no solo en listados populares.  
* A diferencia de Wattpad, se centra en la calidad de la recomendación y en el análisis inteligente de reseñas, no solo en el aspecto social.  
* A diferencia de Kindle, Netbook no está limita el comercio, sino que busca crear un espacio abierto para autores y lectores.  
* Frente a Google Books, se aporta una dimensión comunitaria y de interacción que enriquece la experiencia lectora.

## **4\. Objetivos**

### **Objetivo General 1**

Desarrollar una arquitectura web escalable que permita la gestión integral de libros digitales y la administración de perfiles de usuario diferenciados (lectores y autores independientes), garantizando la accesibilidad y seguridad de los datos.

* **Objetivos específicos 1-0 E1:** Implementar un sistema de autenticación y autorización seguro que distinga entre los roles de lector, autor y administrador para controlar el acceso a las funcionalidades.  
* **Objetivos específicos 1-0 E2:** Desarrollar un módulo de gestión de contenido que permita a los autores subir, editar y eliminar sus obras en formatos digitales (texto o PDF).  
* **Objetivos específicos 1-0 E3:** Crear una interfaz de lectura web que permita a los usuarios buscar libros, visualizar el contenido y organizar sus bibliotecas personales mediante colecciones ("favoritos", "para más tarde", "fantasía, misterio, terror").  
* **Objetivos específicos 1-0 E4:** Construir un panel de administración para supervisar las métricas globales del sistema, como el número de libros, usuarios registrados y valoraciones emitidas.

### **Objetivo General 2**

Integrar servicios de Inteligencia Artificial para resolver el problema de la saturación de información, ofreciendo un sistema de descubrimiento de lecturas personalizado y herramientas de análisis automático de opinión.

* **Objetivos específicos 2-0 E1:** Diseñar y entrenar un algoritmo de recomendación que sugiera libros basándose en el historial de lectura y las valoraciones previas del usuario, diferenciándose de las listas de popularidad estáticas.  
* **Objetivos específicos 2-0 E2:** Implementar un sistema de filtrado colaborativo o de clustering para identificar y conectar usuarios con gustos literarios similares.  
* **Objetivos específicos 2-0 E3:** Desarrollar un servicio de Procesamiento de Lenguaje Natural (NLP) que analice semánticamente las reseñas de los usuarios para extraer tendencias de opinión sobre las obras.

### **Objetivo General 3**

Fomentar la interacción social entre lectores y autores mediante herramientas comunitarias que permitan el feedback directo y la creación de nichos de interés literario.

* **Objetivos específicos 3-0 E1:** Habilitar un sistema de reseñas y valoraciones cualitativas que permita a los lectores ofrecer feedback visible sobre las obras leídas.  
* **Objetivos específicos 3-0 E2:** Crear espacios virtuales o "comunidades" temáticas donde los usuarios puedan debatir y agruparse según sus géneros o autores favoritos.  
* **Objetivos específicos 3-0 E3:** Proporcionar a los autores un panel de estadísticas que muestre el rendimiento de sus obras (lecturas y valoraciones) para ayudarles a entender a su audiencia.

## **5\. Requisitos**

### **Requisitos Funcionales (RF)**

**Módulo de Usuarios y Acceso**

* **RF-01:** El sistema debe permitir el registro y login de usuarios mediante correo y contraseña.  
* **RF-02:** El sistema debe diferenciar permisos según el rol: Lector (lectura/social), Autor (publicación/gestión), Admin (supervisión).

**Módulo de Gestión Bibliográfica (Autores)**

* **RF-03:** El sistema debe permitir la subida de archivos (PDF/TXT) con validación de formato.  
* **RF-04:** El autor debe poder editar metadatos (título, sinopsis, género) y eliminar sus obras.

**Módulo de Lectura (Lectores)**

* **RF-05:** El sistema debe permitir buscar libros por título, género o autor.  
* **RF-06:** El sistema debe disponer de un visor web para leer el contenido sin descargar el archivo.  
* **RF-07:** El usuario debe poder organizar libros en listas ("Favoritos", "Leídos").

**Módulo Social**

* **RF-08:** El usuario debe poder valorar (1-5 estrellas) y comentar libros.  
* **RF-09:** El sistema debe permitir la creación de hilos de discusión o grupos temáticos.

**Módulo de Inteligencia Artificial**

* **RF-10:** El sistema debe mostrar un bloque de "Recomendados para ti" en la portada del usuario.  
* **RF-11:** El sistema debe analizar las reseñas nuevas para extraer una etiqueta de sentimiento (Positivo/Negativo/Neutro) automáticamente.

### **Requisitos No Funcionales (RNF)**

* **RNF-01 (Usabilidad):** La interfaz web debe ser responsive (adaptable a móvil y tablet).  
* **RNF-02 (Rendimiento):** El tiempo de carga del visor de lectura no debe superar los 3 segundos.  
* **RNF-03 (Arquitectura):** El sistema debe implementar una arquitectura desacoplada, utilizando una API REST para la comunicación entre el backend principal y el servicio de IA (Python, Java).  
* **RNF-04 (Seguridad):** Las contraseñas deben almacenarse encriptadas (hash).

### **Requisitos Opcionales (Futuros)**

* **RO-01:** Chatbot asistente para consultas literarias.  
* **RO-02:** OCR para digitalización de imágenes a texto.

## **6\. Matriz de Trazabilidad**

Esta matriz relaciona cada Objetivo Específico (OE) con los Requisitos Funcionales (RF) que lo satisfacen, asegurando que todo el desarrollo está justificado por una meta del proyecto.

| REQUISITOS / OBJETIVOS | OG 1-OE1 | OG 1-OE2 | OG 1-OE3 | OG 1-OE4 | OG 2-OE1 | OG 2-OE2 | OG 2-OE3 | OG 3-OE1 | OG 3-OE2 | OG 3-OE3 |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| **RF-01** (Registro/Login) | X |  |  |  |  |  |  |  |  |  |
| **RF-02** (Roles/Permisos) | X |  |  | X |  |  |  |  |  |  |
| **RF-03** (Subida libros) |  | X |  |  |  |  |  |  |  |  |
| **RF-04** (Gestión obras) |  | X |  |  |  |  |  |  |  | X |
| **RF-05** (Búsqueda) |  |  | X |  |  |  |  |  |  |  |
| **RF-06** (Visor web) |  |  | X |  |  |  |  |  |  |  |
| **RF-07** (Colecciones) |  |  | X |  |  |  |  |  |  |  |
| **RF-08** (Valorar/Reseñar) |  |  |  |  |  |  |  | X |  |  |
| **RF-09** (Comunidades) |  |  |  |  |  |  |  |  | X |  |
| **RF-10** (Recomendación) |  |  | X |  | X |  |  |  |  |  |
| **RF-11** (Análisis Sentim.) |  |  |  |  |  |  | X |  |  |  |
| **RNF-01** (Responsive) |  | X |  |  |  |  |  |  |  |  |
| **RNF-02** (Rendimiento) |  | X |  |  |  |  |  |  |  |  |
| **RNF-03** (Arq API/IA) |  |  | X |  |  |  |  |  |  |  |
| **RNF-04** (Seguridad) | X |  |  |  |  |  |  |  |  |  |

## **7\. Casos de Uso**

### **7.1. Módulo de Acceso**

flowchart LR  
    UI\[Usuario Invitado\] \--\> RC(Recuperar Contraseña)  
    UI \--\> REG(Registrarse)  
    L\[Lector\] \--\> IS(Iniciar Sesión)  
    L \--\> CS(Cerrar Sesión)  
    A\[Autor\] \--\> IS  
    A \--\> CS  
    ADM\[Administrador\] \--\> CS

* **Descripción General:** Gestiona la entrada segura a la plataforma. Valida la identidad y asigna permisos (Roles) mediante un sistema de control de acceso (RBAC). Permite registro y recuperación.  
* **Actores y Roles:**  
  * *Usuario Invitado:* Persona no autenticada (accede a landing).  
  * *Lector:* Registrado. Permisos para leer, reseñar, colecciones.  
  * *Autor:* Registrado. Permisos para subir, gestionar y eliminar sus obras.  
  * *Administrador:* Permisos globales para supervisar el sistema.  
* **Lógica y Flujo de Datos (Login):**  
  1. *Entrada:* El usuario introduce correo y contraseña.  
  2. *Procesamiento:* Backend recibe credenciales. Busca usuario en base de datos. Compara el hash de la contraseña introducida con el hash almacenado.  
  3. *Salida:*  
     * *Éxito:* Genera Token de sesión (JWT) con ID y Rol. Redirige a vista correspondiente.  
     * *Error:* Mensaje genérico ("Credenciales inválidas").

### **7.2. Módulo de Lector (Lectura y Descubrimiento)**

flowchart LR  
    UI\[Usuario Invitado\] \--\> BL(Buscar Libros)  
    UI \--\> VCG(Ver Catálogo General)  
    L\[Lector Logueado\] \--\> LL(Leer Libro \- Visor Web)  
    L \--\> VFD(Ver Ficha Detallada)  
    L \--\> GC(Gestionar Colecciones)  
    L \--\> OR(Obtener Recomendación IA)  
    VFD \-.-\>|extend| CO(Consultar Opiniones)  
    CO \-.-\>|include| GRS(Generar Resumen Sentimientos)  
    SIA\[Servicio IA NLP\] \--\> GRS

* **Descripción General:** Consumo de contenido. Navegación pública y privada. Integración con IA para análisis de sentimientos bajo demanda.  
* **Actores y Roles:**  
  * *Usuario Invitado:* Busca y ve catálogo.  
  * *Lector:* Accede a detalle, lee, solicita análisis IA.  
  * *Servicio IA (Sistema Externo):* API encargada de procesar lenguaje natural.  
* **Lógica y Flujo de Datos:**  
  * **Caso A: Acceso al Detalle y Análisis de Opiniones (Lógica IA)**  
    1. *Solicitud:* Lector selecciona un libro para ver detalles.  
    2. *Verificación:* Sistema comprueba Token. Si no: redirige a Login. Si sí: Procede.  
    3. *Recuperación:* Backend recupera metadatos y listado de reseñas de la BD.  
    4. *Procesamiento IA (On-Demand):* Sistema envía el texto de las reseñas al Servicio de IA (Python). La IA analiza sentimiento global y genera resumen textual.  
    5. *Presentación:* Carga vista "Detalle de Libro" con ficha, botón Leer y "Resumen de Opiniones (IA)".  
  * **Caso B: Lectura**  
    1. *Acción:* Usuario pulsa "Leer Libro".  
    2. *Flujo:* El visor web solicita archivo al servidor de almacenamiento y lo renderiza paginado, sin permitir descarga directa.

### **7.3. Módulo de Autor (Gestión Editorial y Supervisión)**

flowchart LR  
    A\[Autor\] \--\> SNO(Subir Nueva Obra)  
    SNO \-.-\>|include| VF(Validar Formato PDF/TXT)  
    A \--\> EM(Editar Metadatos)  
    A \--\> EOP(Eliminar Obra Propia)  
    A \--\> CE(Consultar Estadísticas)  
    ADM\[Administrador\] \--\> AOS(Auditar Obras Subidas)  
    AOS \-.-\>|extend| BO(Bloquear/Eliminar Obra)

* **Descripción General:** Administración del ciclo de vida de obras de autores. Lógica estricta de validación de archivos.  
* **Actores y Roles:**  
  * *Autor:* Único actor con permisos de escritura sobre sus libros.  
* **Lógica y Flujo de Datos (Subida de Obra):**  
  1. *Entrada:* Autor completa formulario de metadatos y selecciona archivo.  
  2. *Validación (Lógica Crítica):* Verifica tipo MIME y extensión. Regla: Solo .pdf o .txt. Si falla: Detiene proceso.  
  3. *Almacenamiento:* Guarda archivo en sistema de ficheros/nube (ej. S3/Firebase). Metadatos y URL se guardan en Base de Datos Relacional, vinculados al ID del Autor.  
  4. *Confirmación:* Notifica al Autor.

### **7.4. Módulo Social (Social y Moderación)**

flowchart LR  
    L\[Lector\] \--\> VL(Valorar Libro)  
    L \--\> ER(Escribir Reseña)  
    L \--\> PH(Participar en Hilo)  
    L \--\> UC(Unirse a Comunidad)  
    ADM\[Administrador\] \--\> MF(Moderar Foros)  
    ADM \--\> MR(Moderar Reseñas)

* **Descripción General:** Interacción comunitaria. Generación de contenido por el usuario (UGC) que sirve de alimento a la IA.  
* **Actores y Roles:** *Lector* (generador de interacción) y *Administrador*.  
* **Lógica y Flujo de Datos (Escribir Reseña):**  
  1. *Entrada:* Lector otorga puntuación (1-5) y escribe texto.  
  2. *Validación:* Sistema verifica que el usuario no haya reseñado el mismo libro antes.  
  3. *Persistencia:* La reseña se guarda en "bruto" (texto original). Se actualiza la puntuación media. **Nota Importante:** NO se ejecuta análisis de IA aquí.  
  4. *Feedback:* Muestra reseña publicada.

## **8\. Diagrama de Clases y Bocetos de Interfaz**

Siguiendo la metodología de Diseño basado en Interfaz, definimos las clases extrayendo atributos de las vistas:

### **8.1. Entidad: Usuario**

**Boceto de Interfaz:**

\[ PERFIL DE USUARIO \]  
Usuario: \[daniel\_dev\]  
Email: \[daniel@correo.com\]  
Contraseña: \[\*\*\*\*\*\*\*\*\*\]  
Rol: \[Autor\]  
Registrado en: \[12/05/2025\]  
Estadísticas:   
\- 3 Libros publicados  
\- 15 Reseñas escritas  
\- 2 Colecciones creadas

**Clase Resultante:**

* id: Long  
* nombre: String  
* email: String  
* passwordHash: String  
* rol: Enum (LECTOR, AUTOR, ADMIN)  
* avatarUrl: String  
* fechaRegistro: DateTime  
* numeroLibrosPublicados: Integer (Atributo Derivado)  
* numeroResenas: Integer (Atributo Derivado)  
* numeroColecciones: Integer (Atributo Derivado)

### **8.2. Entidad: Libro**

**Boceto de Interfaz:**

\[ DETALLE DE OBRA \]  
Título: \[El Viento en las Cenizas\]  
Autor: \[@daniel\_dev\]  
Género: \[Fantasía\]  
Sinopsis: \[Texto largo descriptivo...\]  
Archivo: \[archivo\_viento.pdf\]  
Valoración: \[★★★★☆ (4.2)\]  
Resumen IA: \["Los lectores destacan la trama..."\]  
Estado: \[PUBLICADO\]

**Clase Resultante:**

* id: Long  
* titulo: String  
* sinopsis: Text  
* genero: String  
* portadaUrl: String  
* archivoUrl: String  
* fechaSubida: DateTime  
* estado: Enum (PUBLICO, BORRADOR, BLOQUEADO\_ADMIN)  
* /mediaValoracion: Double (Atributo Derivado)  
* resumenIA: Text  
* autor: Usuario (Referencia)

### **8.3. Entidad: Reseña**

**Boceto de Interfaz:**

\[ TU OPINIÓN \]  
Puntuación: \[★★★★☆\] (1-5)  
Tu Comentario: \["Me ha encantado..."\]  
Sentimiento (IA): \[Oculto al usuario: POSITIVO\]

**Clase Resultante:**

* id: Long  
* puntuacion: Integer  
* comentario: Text  
* fecha: DateTime  
* sentimientoIA: Enum (POSITIVO, NEGATIVO, NEUTRO)  
* autor: Usuario (Referencia)  
* libro: Libro (Referencia)

### **8.4. Entidad: Colección**

**Boceto de Interfaz:**

\[ MIS COLECCIONES \]  
Lista: \["Lecturas de Verano"\]  
Visibilidad: \[Privada\]  
Propietario: \[@daniel\_dev\]  
Total libros: \[5 libros\] \<-- Calculado

**Clase Resultante:**

* id: Long  
* nombre: String  
* esPublica: Boolean  
* fechaCreacion: DateTime  
* propietario: Usuario (Referencia)  
* /numeroLibros: Integer (Atributo Derivado)

### **8.5. Entidades Sociales (Comunidad, Membresía y MensajeForo)**

**Boceto de Interfaz:**

\[ FORO: FANTASÍA ÉPICA \]  
Descripción: \["Debates sobre magia..."\]  
Miembro: @daniel\_dev | ROL: Moderador | Uniones: 01/01/26  
Mensaje \#12 \- 02/01/26  
Autor: @daniel\_dev  
Texto: "¿Qué os pareció el final?"

**Clases Resultantes:**

* **Comunidad:** id (Long), nombre (String), descripcion (String), tema (String)  
* **Membresia:** fechaUnion (DateTime), rolComunidad (Enum: MIEMBRO/MODERADOR), usuario (Usuario), comunidad (Comunidad)  
* **MensajeForo:** id (Long), contenido (Text), fechaCreacion (DateTime), autor (Usuario), comunidad (Comunidad)

### **Diagrama de Clases UML**

classDiagram  
    class Usuario {  
        \-Long id  
        \-String nombre  
        \-String email  
        \-String passwordHash  
        \-Enum rol  
        \-DateTime fechaRegistro  
        \+getNumeroLibrosPublicados() Integer  
        \+getNumeroResenas() Integer  
        \+getNumeroColecciones() Integer  
    }  
    class Libro {  
        \-Long id  
        \-String titulo  
        \-Text sinopsis  
        \-String archivoUrl  
        \-Enum estado  
        \-Double mediaValoracion  
        \-Text resumenIA  
    }  
    class Resena {  
        \-Long id  
        \-Integer puntuacion  
        \-Text comentario  
        \-DateTime fecha  
        \-Enum sentimientoIA  
    }  
    class Coleccion {  
        \-Long id  
        \-String nombre  
        \-Integer numeroLibros  
    }  
    class Comunidad {  
        \-Long id  
        \-String nombre  
        \-String descripcion  
    }  
    class Membresia {  
        \-DateTime fechaUnion  
        \-Enum rolComunidad  
    }  
    class MensajeForo {  
        \-Long id  
        \-Text contenido  
        \-DateTime fechaCreacion  
    }

    Usuario "1" \-- "\*" Libro : publica \>  
    Usuario "1" \-- "\*" Resena : autor \>  
    Libro "1" \-- "\*" Resena : sobre \>  
    Usuario "1" \-- "\*" Coleccion : propietario \>  
    Coleccion "\*" \-- "\*" Libro : contiene \>  
    Usuario "1" \-- "\*" Membresia : usuario \>  
    Comunidad "1" \-- "\*" Membresia : comunidad \>  
    Usuario "1" \-- "\*" MensajeForo : autor \>  
    Comunidad "1" \-- "\*" MensajeForo : contiene \>

## **9\. Modelo Relacional de Base de Datos**

Tras diseñar el Modelo Orientado a Objetos (Diagrama de Clases), procedemos a realizar el mapeo Objeto-Relacional para definir la persistencia de datos.

erDiagram  
    usuarios {  
        INT id\_usuario PK  
        VARCHAR(100) nombre  
        VARCHAR(150) email  
        VARCHAR(255) password\_hash  
        VARCHAR(20) rol  
        VARCHAR(255) avatar\_url  
        DATETIME fecha\_registro  
    }  
    libros {  
        INT id\_libro PK  
        INT id\_autor FK  
        VARCHAR(200) titulo  
        TEXT sinopsis  
        VARCHAR(50) genero  
        VARCHAR(255) portada\_url  
        VARCHAR(255) archivo\_url  
        VARCHAR(20) estado  
        TEXT resumen\_ia  
        DATETIME fecha\_subida  
    }  
    resenas {  
        INT id\_resena PK  
        INT id\_usuario FK  
        INT id\_libro FK  
        INT puntuacion  
        TEXT comentario  
        VARCHAR(20) sentimiento\_ia  
        DATETIME fecha  
    }  
    colecciones {  
        INT id\_coleccion PK  
        INT id\_usuario FK  
        VARCHAR(100) nombre  
        BOOLEAN es\_publica  
        DATETIME fecha\_creacion  
    }  
    colecciones\_libros {  
        INT id\_coleccion PK,FK  
        INT id\_libro PK,FK  
        DATETIME fecha\_agregado  
    }  
    comunidades {  
        INT id\_comunidad PK  
        VARCHAR(100) nombre  
        TEXT descripcion  
        VARCHAR(50) tema  
    }  
    membresias {  
        INT id\_usuario PK,FK  
        INT id\_comunidad PK,FK  
        VARCHAR(20) rol\_comunidad  
        DATETIME fecha\_union  
    }  
    mensajes\_foro {  
        INT id\_mensaje PK  
        INT id\_autor FK  
        INT id\_comunidad FK  
        TEXT contenido  
        DATETIME fecha\_creacion  
    }

    usuarios ||--o{ libros : "1:N (publica)"  
    usuarios ||--o{ resenas : "1:N (escribe)"  
    libros ||--o{ resenas : "1:N (recibe)"  
    usuarios ||--o{ colecciones : "1:N (crea)"  
    colecciones ||--o{ colecciones\_libros : "1:N (contiene)"  
    libros ||--o{ colecciones\_libros : "1:N (pertenece)"  
    usuarios ||--o{ mensajes\_foro : "1:N (aloja)"  
    comunidades ||--o{ mensajes\_foro : "1:N (tiene)"  
    usuarios ||--o{ membresias : "1:N (se une)"  
    comunidades ||--o{ membresias : "1:N (tiene)"

## **10\. Arquitectura de Software y Stack Tecnológico**

Para garantizar que el sistema sea escalable, mantenible y cumpla con los requisitos no funcionales (RNF-03: Arquitectura desacoplada), NetBook no se construirá como un monolito tradicional, sino siguiendo una Arquitectura Desacoplada basada en el consumo de APIs REST, con un enfoque de microservicios para la Inteligencia Artificial.

### **10.1. Patrón Arquitectónico**

El sistema se divide en tres bloques físicos y lógicos independientes:

1. **Aplicación Cliente (Frontend):** Se encargará exclusivamente de la Interfaz de Usuario (UI) y la Experiencia de Usuario (UX). Consumirá los datos del servidor a través de peticiones HTTP.  
2. **Servidor Principal (Backend \- Core):** Actuará como el cerebro del sistema. Internamente, aplicará Arquitectura de 3 Capas (N-Tier):  
   * *Capa de Controladores (API/Controllers):* Expone endpoints y gestiona peticiones/respuestas HTTP.  
   * *Capa de Negocio (Services):* Contiene reglas de negocio (ej. permisos para borrar o hash de contraseñas).  
   * *Capa de Acceso a Datos (Repositories):* Consultas a la base de datos mediante un ORM.  
3. **Servidor de Inteligencia Artificial (Microservicio NLP):** Servicio independiente y especializado que recibirá textos de reseñas desde el servidor principal, los analizará semánticamente y devolverá el resultado (Positivo/Negativo/Neutro).

### **10.2. Stack Tecnológico Seleccionado**

La elección se basa en la especialización: la mejor herramienta para cada tarea específica.

| Capa Arquitectónica | Tecnología Elegida |
| :---- | :---- |
| **Frontend (Cliente)** | React.js (o Angular) \+ Tailwind CSS |
| **Backend Core** | Java \+ Spring Boot |
| **Microservicio IA** | Python \+ FastAPI |
| **Base de Datos** | MySQL (o PostgreSQL) |

## **11\. Planificación y Cronograma de Desarrollo**

Para garantizar la viabilidad y cumplir con la fecha estipulada para finales de abril, se adopta metodología ágil (adaptada a único desarrollador) priorizando **API-First**.

### **Herramientas de Planificación**

1. **Notion:** Centro de documentación estática (diario de desarrollo, apuntes de arquitectura, retrospectivas).  
2. **GitHub Projects:** Gestión operativa mediante Kanban (Backlog, En progreso, Listo), integrado con commits y ramas. (Tablero Kanban: https://github.com/users/xdojebal477-hub/projects/1)

### **Metodología de Trabajo: API-First**

A diferencia del desarrollo en cascada, este proyecto aplicará diseño basado en contratos (API-First). El primer paso del desarrollo backend será definir openapi.yaml (OpenAPI/Swagger). Esto permitirá:

1. Tener un contrato claro de los endpoints, schemas (JSON) y códigos HTTP.  
2. Levantar un servidor Mock para que el Frontend consuma datos simulados, trabajando en paralelo real con el Backend.

### **Definición del Producto Mínimo Viable (MVP)**

Para mitigar riesgos frente a fechas:

* **MVP (Sprints 1-3):** Autenticación, gestión de BD, subida de libros (PDF/TXT), visor de lectura y catálogo básico.  
* **Fase 2 (Sprints 4-6):** Inteligencia Artificial (Microservicio NLP), comunidades, foros y reseñas.

### **Desglose de Tareas y Cronograma (Sprints de 1 semana)**

Desarrollo comprende 6 semanas (23 Mar al 30 Abr). Mayo para documentación final.

* **SPRINT 1: Base de Datos, Contrato API y Autenticación (23 Mar \- 29 Mar)**  
  * DOC-01: Definición del contrato openapi.yaml (Endpoints de Auth y Usuarios).  
  * BDD-01: Ejecutar script SQL de creación de BBDD y tablas principales (Usuarios, Roles).  
  * FRONT-01: Configurar proyecto React/Angular, routing y generar mocks a partir del OpenAPI para Login/Registro.  
  * BACK-01: Configurar proyecto Spring Boot (dependencias, conexión a MySQL, JPA Models).  
  * BACK-02: Implementar Spring Security, JWT y endpoints reales de Login/Registro cumpliendo contrato.  
* **SPRINT 2: Gestión de Catálogo y Autores (30 Mar \- 5 Abr)**  
  * DOC-02: Ampliar openapi.yaml con endpoints de Libros (CRUD).  
  * BDD-02: Crear tablas de Libros y FKs.  
  * FRONT-02: Panel del Autor: Formulario para publicar nueva obra (conectado al Mock).  
  * BACK-03: Lógica de validación de archivos (PDF/TXT), subida al servidor y creación de entidad Libro.  
  * FRONT-03: Vista de Catálogo General (Buscador/Listado público).  
* **SPRINT 3: Experiencia de Lectura y Colecciones (6 Abr \- 12 Abr)**  
  * DOC-03: Ampliar openapi.yaml con endpoints de Colecciones.  
  * BDD-03: Crear tablas de Colecciones y Colecciones\_Libros.  
  * BACK-04: Endpoints para gestión de colecciones y endpoint de streaming seguro del archivo físico.  
  * FRONT-04: Maquetar la vista de "Detalle de Obra" e integrarla con Backend.  
  * FRONT-05: Implementar el Visor Web de lectura (visor PDF o renderizado TXT).  
* **SPRINT 4: Microservicio de Inteligencia Artificial (13 Abr \- 19 Abr)**  
  * IA-01 (3h): Inicializar microservicio Python (FastAPI).  
  * IA-02 (6h): Integrar librería NLP para procesar textos y devolver Sentimiento (Positivo/Negativo/Neutro).  
  * BACK-05: Configurar cliente HTTP en Spring Boot (WebClient) para comunicarse internamente con el microservicio Python.  
  * *(Hito: Fin del desarrollo del motor principal y de IA).*  
* **SPRINT 5: Módulo Social (Comunidades y Reseñas) (20 Abr \- 26 Abr)**  
  * DOC-04: Finalizar contrato OpenAPI con endpoints Sociales.  
  * BDD-04: Desplegar tablas de Reseñas, Comunidades, Mensajes y Membresías.  
  * BACK-06: Endpoints para valorar libros, escribir reseñas y publicar en Foros.  
  * FRONT-06: Integrar formulario de valoración y maquetar vista de Comunidad/Hilos de mensajes.  
* **SPRINT 6: Integración Final, QA y Despliegue (27 Abr \- 30 Abr)**  
  * QA-01: Pruebas de integración entre Frontend real y Backend real (sin Mocks).  
  * QA-02: Corrección de bugs menores y revisión de UI/UX (Responsive).  
  * OPS-01: Despliegue de Base de datos y Backend Spring Boot (ej. Railway/Render).  
  * OPS-02: Despliegue de IA (FastAPI) y Frontend (Vercel/Netlify).  
  * *(Hito: 30 de abril \- Desarrollo cerrado).*  
* **Fase de Documentación y Cierre (1 May \- 18 May)**  
  * Redacción final de la Memoria del Proyecto.  
  * Preparación de la presentación visual y ensayo de la defensa para el tribunal.  
  * Entrega prevista: 18 de mayo.