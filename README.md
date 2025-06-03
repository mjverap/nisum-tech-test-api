# Nisum Tech Test API

Proyecto que permite crear usuarios con Spring Boot, JWT y documentación Swagger.

## Requisitos

- Java 17
- Gradle

## Ejecución

```bash
./gradlew bootRun
```
## Descripción
Esta API permite gestionar usuarios con autenticación JWT. Por el momento incluye endpoints para crear y listar usuarios, así como autenticación y documentación Swagger.
- La API estará disponible en: http://localhost:8080

### Documentación Swagger
- Accede a la documentación interactiva en: http://localhost:8080/swagger-ui.html o http://localhost:8080/swagger-ui/index.html  

### Base de datos
- Se utiliza H2 embebido. Consola disponible en: http://localhost:8080/h2-console  

### Autenticación
- La API utiliza JWT para autenticación. Obtén un token en el endpoint /login.  

### Variables importantes
* Configuración en src/main/resources/application.properties
* Clave secreta JWT: app.security.secret-key

### Scripts útiles
* Ejecutar tests: `./gradlew test`
* Limpiar proyecto: `./gradlew clean`
* Compilar proyecto: `./gradlew build`
* Iniciar servidor: `./gradlew bootRun`


## Endpoints disponibles
- `POST /api/users`: Crear un nuevo usuario.
- `GET /api/users`: Listar todos los usuarios.
- `POST /api/login`: Autenticar un usuario y obtener un token JWT.

### Diagrama de solución
![Diagrama de solución](https://raw.githubusercontent.com/mjverap/nisum-tech-test-api/refs/heads/master/diagrama-solucion.png)

#### Autor 
[María José Vera](https://github.com/mjverap)
