# 📊 TrackUp

TrackUp es una aplicación web multiplataforma desarrollada con Spring Boot, que permite a los usuarios gestionar hábitos saludables y objetivos personales.

## 📝 Índice

1. [Descripción](#descripción)
2. [Características principales](#características-principales)
3. [Tecnologías](#tecnologías)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Requisitos previos](#requisitos-previos)
6. [Instalación y configuración](#instalación-y-configuración)
7. [Configuración de la base de datos](#configuración-de-la-base-de-datos)
8. [Ejecución del proyecto](#ejecución-del-proyecto)
9. [Uso de la aplicación](#uso-de-la-aplicación)
10. [Uso de la API (Swagger)](#uso-de-la-api-swagger)
11. [Endpoints principales](#endpoints-principales)
12. [Pruebas](#pruebas)
13. [Mantenimiento y evolución](#mantenimiento-y-evolución)
14. [Solución de problemas](#solución-de-problemas)
15. [Autor](#autor)
16. [Licencia](#licencia)

## Descripción

TrackUp ayuda a los usuarios a adoptar y mantener hábitos saludables mediante:

- Registro y seguimiento diario de hábitos
- Gestión de objetivos personales
- Visualización de estadísticas y gráficos de progreso
- Plataforma intuitiva y responsive

> 🚀 **Nota:** Este README ofrece un resumen de alto nivel. La documentación completa (PDF) está disponible en la carpeta /docs.

## Documentación completa

Para un detalle exhaustivo de cada sección (introducción, análisis, diseño, implementación, pruebas, mantenimiento y conclusiones), consulta el PDF: `/docs/AnexoI-MunozPanaderoAlvaro.pdf`

## Características principales

- CRUD de **Usuarios**
- CRUD de **Hábitos** y **Tipos de hábitos**
- Registro diario de cumplimiento
- Gestión de **Objetivos** (Goals)
- Documentación interactiva con Swagger/OpenAPI
- Autenticación JWT
- Interfaz web responsive

## Tecnologías

- **Backend**: Java 21, Spring Boot 3.4.4, Spring Data JPA, Spring Security (JWT)
- **Frontend**: HTML, CSS, JavaScript (renderizado desde Spring)
- **Base de datos**: MySQL (producción), H2 (desarrollo)
- **Contenedores**: Docker & Docker Compose
- **Documentación**: springdoc-openapi UI (Swagger)
- **Testing**: JUnit, Mockito, Postman

## Estructura del proyecto

```
trackup/
├── src/main/java/trackup
│   ├── controller          # Controladores REST
│   ├── dto
│   │   ├── request         # DTOs de entrada
│   │   └── response        # DTOs de salida
│   ├── entity              # Entidades JPA
│   ├── repository          # Repositorios Spring Data
│   ├── service             # Interfaces de la lógica de negocio
│   │   └── impl           # Implementaciones de los servicios
│   └── TrackUpApplication.java # Clase principal
├── src/main/resources
│   └── application.properties # Configuración
├── scripts/
│   ├── docker-compose.yml  # Configuración Docker
│   └── init.sql           # Script de inicialización de BD
└── docs/                   # Documentación del proyecto
```

## Requisitos previos

Antes de comenzar, asegúrate de tener instalado:

- **Java 21** o superior
- **Maven 3.8+** 
- **Docker** y **Docker Compose**
- **Git**

### Verificar instalaciones

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Docker
docker --version
docker-compose --version
```

## Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/trackup.git
cd trackup
```

### 2. Descargar dependencias

```bash
mvn clean install
```

## Configuración de la base de datos

### Opción 1: Usar Docker (Recomendado)

#### 1. Levantar el contenedor MySQL

```bash
# Navegar a la carpeta scripts
cd scripts

# Levantar el contenedor MySQL
docker-compose up -d
```

#### 2. Verificar que el contenedor esté ejecutándose

```bash
docker ps
```

Deberías ver un contenedor MySQL ejecutándose en el puerto 3306.

#### 3. Verificar la conexión (opcional)

```bash
# Conectarse a MySQL desde línea de comandos
docker exec -it trackup-mysql mysql -u trackup_user -p
# Contraseña: trackup_password
```

### Opción 2: Usar H2 (Desarrollo) (no es el sistema actual)

Si prefieres usar H2 para desarrollo local, modifica el archivo `application.properties`:

```properties
# Configuración H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Habilitar consola H2
spring.h2.console.enabled=true
```

### Configuración de propiedades

El archivo `application.properties` debe contener:

```properties
# Configuración del servidor
server.port=8080

# Configuración MySQL (Docker)
spring.datasource.url=jdbc:mysql://localhost:3306/trackup_db
spring.datasource.username=trackup_user
spring.datasource.password=trackup_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Configuración JWT
app.jwt.secret=tu-clave-secreta-jwt
app.jwt.expiration=86400000

# Configuración Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## Ejecución del proyecto

### 1. Asegurar que MySQL esté ejecutándose

```bash
# Si usas Docker
cd scripts
docker-compose up -d

# Verificar estado
docker ps
```

### 2. Ejecutar la aplicación

```bash
# Desde la raíz del proyecto
mvn spring-boot:run
```

O alternativamente:

```bash
# Compilar y ejecutar JAR
mvn clean package
java -jar target/trackup-0.0.1-SNAPSHOT.jar
```

### 3. Verificar que la aplicación esté funcionando

- **Aplicación web**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## Uso de la aplicación

### Registro de usuario

1. Accede a http://localhost:8080
2. Haz clic en "Registrarse"
3. Completa el formulario con tus datos
4. Confirma tu registro

### Iniciar sesión

1. Usa tus credenciales para iniciar sesión
2. Serás redirigido al dashboard principal

### Gestión de hábitos

- **Crear hábito**: Botón "Nuevo Hábito" en el dashboard
- **Registrar cumplimiento**: Marca los hábitos completados cada día
- **Ver estadísticas**: Accede a gráficos de progreso
- **Editar/Eliminar**: Opciones disponibles en cada hábito

### Gestión de objetivos

- **Crear objetivo**: Sección "Objetivos" en el menú
- **Seguimiento**: Visualiza el progreso hacia tus metas
- **Completar**: Marca objetivos como completados

## Uso de la API (Swagger)

### Acceder a la documentación

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Documentación PDF**: `/docs/swagger-documentation.pdf`

### Autenticación API

1. Regístrate o inicia sesión para obtener un token JWT
2. En Swagger, haz clic en "Authorize"
3. Introduce el token en formato: `Bearer tu-token-jwt`
4. Ahora puedes probar todos los endpoints

## Mantenimiento y evolución

### Escalabilidad
- Arquitectura modular para añadir retos, sistema de puntos
- Separación clara entre capas (controller, service, repository)
- DTOs para comunicación API limpia

### Mejoras futuras
- Integración con notificaciones push
- Internacionalización (i18n)
- Autenticación social (Google, Facebook)
- Aplicación móvil nativa
- Análisis avanzado de datos

### Backup de datos

```bash
# Crear backup de la base de datos
docker exec trackup-mysql mysqldump -u trackup_user -ptrackup_password trackup_db > backup.sql

# Restaurar backup
docker exec -i trackup-mysql mysql -u trackup_user -ptrackup_password trackup_db < backup.sql
```

## Solución de problemas

### Problemas comunes

#### Error de conexión a la base de datos
```bash
# Verificar que MySQL esté ejecutándose
docker ps

# Reiniciar contenedor si es necesario
docker-compose down
docker-compose up -d
```

#### Puerto 8080 ocupado
```bash
# Cambiar puerto en application.properties
server.port=8081

# O terminar proceso que usa el puerto
sudo lsof -ti:8080 | xargs kill -9
```

#### Problemas de permisos con Docker
```bash
# Añadir usuario al grupo docker
sudo usermod -aG docker $USER

# Reiniciar sesión o ejecutar
newgrp docker
```

### Logs y depuración

```bash
# Ver logs de la aplicación
mvn spring-boot:run | grep -i error

# Ver logs del contenedor MySQL
docker logs trackup-mysql

# Habilitar logs detallados en application.properties
logging.level.trackup=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Restablecer base de datos

```bash
# Eliminar contenedor y volumen
docker-compose down -v

# Recrear contenedor
docker-compose up -d
```

## Autor

**Álvaro Muñoz Panadero** (2º DAM)  
📧 alvaromp.dev@gmail.com  
🌐 [GitHub](https://github.com/alvarompdev)

## 🚀 ¡Listo para usar TrackUp!

Si sigues estos pasos, tendrás TrackUp funcionando en tu máquina local. Para cualquier problema o pregunta, no dudes en abrir un issue en el repositorio.

**¡Comienza a trackear tus hábitos hoy mismo! 💪**