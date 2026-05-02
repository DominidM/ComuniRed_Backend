# ComuniRed Backend — Arquitectura

## Stack

- **Java 21** + **Spring Boot 3.5.5**
- **MongoDB Atlas** (base de datos principal)
- **GraphQL** (consultas complejas con relaciones)
- **REST** (operaciones simples, archivos, auth)
- **WebSocket / STOMP** (mensajería en tiempo real)
- **Cloudinary** (almacenamiento de imágenes)
- **MapStruct** (mapeo entre capas)
- **Lombok** (reducción de boilerplate)

---

## Patrón arquitectónico

**Hexagonal (Ports & Adapters) con Vertical Slicing por dominio.**

Cada dominio es un módulo independiente con sus propias capas. El dominio no conoce Spring, MongoDB ni ningún framework. Todo lo externo entra y sale por interfaces (ports).

```
Un dominio no puede importar clases de otro dominio.
La comunicación entre dominios se hace exclusivamente por eventos.
```

---

## Estructura de carpetas

```
src/main/java/com/comunired/
│
├── usuarios/
├── quejas/
├── historias/
├── mensajeria/
└── shared/
    ├── security/
    └── config/
```

### Estructura interna de cada dominio

```
{dominio}/
├── application/
│   ├── dto/
│   │   ├── in/          ← Commands (entrada)
│   │   └── out/         ← Responses (salida)
│   ├── mapper/          ← domain → dto
│   ├── port/
│   │   ├── in/          ← Casos de uso (interfaces)
│   │   └── out/         ← Puertos de salida (interfaces)
│   └── service/
│       ├── command/     ← Escritura (crear, actualizar, eliminar)
│       └── query/       ← Lectura (obtener, buscar)
│
├── domain/
│   ├── entity/          ← Java puro, sin anotaciones de framework
│   └── event/           ← Eventos de dominio (records)
│
└── infrastructure/
    ├── adapter/
    │   ├── in/
    │   │   ├── rest/    ← Controllers REST
    │   │   └── graphql/ ← Resolvers GraphQL (si aplica)
    │   └── out/
    │       ├── persistence/  ← Document + MongoRepository + Adapter
    │       └── cloudinary/   ← CloudinaryAdapter
    └── mapper/          ← domain → document (infra)
```

---

## Decisión por protocolo

| Protocolo | Cuándo usarlo |
|-----------|---------------|
| **REST** | Auth, subida de archivos (multipart), operaciones simples (PATCH, DELETE) |
| **GraphQL** | Consultas con relaciones entre dominios (queja + usuario + categoría en una sola query) |
| **WebSocket** | Mensajería en tiempo real, notificaciones push |

### Regla práctica

- Lleva imagen → **REST**
- Es un join entre 2+ colecciones → **GraphQL**
- Necesita push al cliente → **WebSocket**

---

## GraphQL — Schema dividido por dominio

```
resources/graphql/
├── schema.graphqls       ← solo scalars + type Query/Mutation/Subscription vacíos
├── usuario.graphqls      ← extend type Query / Mutation
├── queja.graphqls        ← extend type Query / Mutation
├── mensaje.graphqls      ← extend type Query / Mutation / Subscription
├── seguimiento.graphqls  ← extend type Query / Mutation
└── historia.graphqls     ← solo type Historia (REST puro)
```

Spring Boot detecta y combina automáticamente todos los `.graphqls` en `resources/graphql/`.

---

## Eventos de dominio

Comunicación entre dominios mediante `ApplicationEventPublisher` de Spring (síncrono, mismo proceso). No se usa Kafka ni RabbitMQ por ahora.

| Evento | Emisor | Listeners |
|--------|--------|-----------|
| `QuejaCreada` | quejas | notificaciones, historial_evento |
| `QuejaEstadoCambiado` | quejas | notificaciones, mensajeria (mensaje automático) |
| `QuejaVotada` | quejas | notificaciones |
| `HistoriaPublicada` | historias | notificaciones |
| `HistoriaExpirada` | historias | historias (desactiva via TTL) |
| `UsuarioRegistrado` | usuarios | email (bienvenida), notificaciones |
| `UsuarioSiguio` | usuarios | notificaciones |
| `MensajeRecibido` | mensajeria | websocket (push) |

### Regla para usar eventos

> Si después de una acción necesitas hacer 2 o más cosas en dominios distintos → evento.
> Si es una sola cosa → llama el servicio directo.

---

## Módulo historias (implementado)

Primer módulo construido con la arquitectura completa como referencia.

**Endpoints REST:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/historias` | Crear historia (multipart) |
| `GET` | `/api/historias?usuarioId=` | Obtener historias activas |
| `PATCH` | `/api/historias/{id}/vista` | Marcar como vista |
| `DELETE` | `/api/historias/{id}` | Eliminar historia propia |

**TTL automático:** MongoDB borra las historias a las 24h via índice `fechaExpiracion`.

**Evento emitido:** `HistoriaPublicada` al crear.

---

## Convenciones

```java
// Entidad de dominio — Java puro
public class Historia {
    public static Historia crear(...) { }      // factory method
    public static Historia reconstruir(...) { } // desde persistencia
    public void marcarVista(String usuarioId) { } // comportamiento
}

// Evento — record inmutable
public record HistoriaPublicada(String historiaId, String usuarioId, ...) {}

// Port de entrada — interfaz del caso de uso
public interface CrearHistoriaUseCase {
    HistoriaResponse ejecutar(CrearHistoriaCommand command);
}

// Service — implementa el port de entrada
@Service
public class CrearHistoriaService implements CrearHistoriaUseCase { }

// Adapter de salida — implementa el port de salida
@Component
public class HistoriaMongoAdapter implements HistoriaRepositoryPort { }
```

---

## Pendiente

- [ ] Módulo `quejas` con arquitectura hexagonal
- [ ] Módulo `mensajeria` con WebSocket
- [ ] `@AuthenticationPrincipal` en controllers (reemplazar `@RequestParam usuarioId`)
- [ ] Migrar `@Indexed(expireAfterSeconds)` deprecado a `@TimeToLive`
