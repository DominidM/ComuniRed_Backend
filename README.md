# ComuniRed Backend

Este proyecto es el backend para la plataforma **ComuniRed** desarrollado por SolucionesWeb.  
Proporciona servicios RESTful para la gestión de usuarios, roles, autenticación, y otras funcionalidades de la plataforma.

## Tecnologías utilizadas

- **Java 21.0.7**
- **Spring Boot**
- **MongoDB**
- **BCrypt** (seguridad en contraseñas)
- **Maven 3.9.9**

## Instalación

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/SolucionesWeb/ComuniRed_Backend.git
   cd ComuniRed_Backend
   ```

2. **Configura MongoDB**
   - Asegúrate de tener un servidor MongoDB corriendo.
   - Edita el archivo de configuración `application.properties`:
     ```
     spring.data.mongodb.uri=mongodb://localhost:27017/comunired
     ```

3. **Compila el proyecto**
   ```bash
   mvn clean install
   ```

4. **Ejecuta el backend**
   ```bash
   mvn spring-boot:run
   ```
   - Por defecto, el backend corre en el puerto `8080`.

## Endpoints principales

- `/api/usuarios` - Gestión de usuarios
- `/api/auth/login` - Autenticación
- `/api/roles` - Gestión de roles

## Seguridad

Las contraseñas se almacenan usando hashes bcrypt.  
Para login seguro, envía el usuario y contraseña en texto plano vía POST (HTTPS recomendado).

## Ejemplo de solicitud de login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@ejemplo.com",
  "password": "miclave"
}
```

## Contribuciones

¡Se aceptan pull requests!  
Por favor, abre una issue para reportar bugs o sugerir mejoras.

## Licencia

Este proyecto está bajo licencia MIT.

---

**Desarrollado por SolucionesWeb**