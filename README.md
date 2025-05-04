# 📊 TrackUp

TrackUp es una aplicación web multiplataforma desarrollada con Spring Boot, que permite a los usuarios gestionar hábitos saludables y objetivos personales.

## 📝 Índice

1. [Descripción](#descripción)
2. [Características principales](#características-principales)
3. [Tecnologías](#tecnologías)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Instalación y ejecución](#instalación-y-ejecución)
6. [Uso de la API (Swagger)](#uso-de-la-api-swagger)
7. [Endpoints principales](#endpoints-principales)
8. [Pruebas](#pruebas)
9. [Mantenimiento y evolución](#mantenimiento-y-evolución)
10. [Autor](#autor)
11. [Licencia](#licencia)

## Descripción

TrackUp ayuda a los usuarios a adoptar y mantener hábitos saludables mediante:

- Registro y seguimiento diario de hábitos  
- Gestión de objetivos personales  
- Visualización de estadísticas y gráficos de progreso  
- Plataforma intuitiva y responsive  

> 🚀 **Nota:** Este README ofrece un resumen de alto nivel. La documentación completa (PDF) está disponible en la carpeta `/docs`.

## Documentación completa

Para un detalle exhaustivo de cada sección (introducción, análisis, diseño, implementación, pruebas, mantenimiento y conclusiones), consulta el PDF:

```
/docs/AnexoI-MunozPanaderoAlvaro.pdf
```

## Características principales

- CRUD de **Usuarios**  
- CRUD de **Hábitos** y **Tipos de hábitos**  
- Registro diario de cumplimiento  
- Gestión de **Objetivos** (Goals)  
- Documentación interactiva con Swagger/OpenAPI  

## Tecnologías

- **Backend**: Java 21, Spring Boot 3.4.4, Spring Data JPA, Spring Security (JWT)  
- **Frontend**: HTML, CSS, JavaScript (renderizado desde Spring)  
- **Base de datos**: H2 (desarrollo), configurable a PostgreSQL u otra  
- **Documentación**: springdoc-openapi UI (Swagger)  
- **Testing**: JUnit, Mockito, Postman  

## Estructura del proyecto

```
trackup/
├── src/main/java/trackup
│   ├── controller      # Controladores REST
│   ├── dto
│   │   ├── request     # DTOs de entrada
│   │   └── response    # DTOs de salida
│   ├── entity          # Entidades JPA
│   ├── repository      # Repositorios Spring Data
│   ├── service         # Interfaces de la lógica de negocio
│   │   └── impl        # Implementaciones de los servicios
│   └── TrackUpApplication.java  # Clase principal
└── src/main/resources
    └── application.properties # Configuración (puerto, H2, JWT...)
```

<!--
## Instalación y ejecución

1. Clona el repositorio:  
   ```bash
   git clone https://github.com/tu-usuario/trackup.git
   cd trackup
   ```
2. Ejecuta con Maven:  
   ```bash
   ./mvnw spring-boot:run
   ```
3. La aplicación estará disponible en `http://localhost:8080`.
-->

## Uso de la API (Swagger)

Accede a la documentación interactiva:

```
http://localhost:8080/swagger-ui.html
```

O si lo desea, también puede consultar un PDF con el mismo contenido en:

```
/docs/swagger-documentation.pdf
```

Así podrás ver y probar todos los endpoints.

<!--
## Pruebas

- **Unitarias**: JUnit + Mockito para servicios y controladores.  
- **Integración**: `@SpringBootTest` para flujos completos.  
- **Manual**: Colecciones de Postman disponibles en `/postman`.  

> En próximas entregas se ampliarán las pruebas unitarias y de integración.
-->

## Mantenimiento y evolución

- **Escalabilidad**: arquitectura modular para añadir retos, sistema de puntos…  
- **Mejoras futuras**: integración con notificaciones push, internacionalización, autenticación social  

## Autor

Álvaro Muñoz Panadero (2º DAM) – alvaromp.dev@gmail.com

<!-- 
## Licencia

Este proyecto está bajo licencia MIT. (Ver archivo `LICENSE`)
-->