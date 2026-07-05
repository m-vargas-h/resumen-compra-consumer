# resumen-compra-consumer

Microservicio consumidor dentro de la arquitectura de mensajería asíncrona de **PlataformaEducativa** (CDY2204 – Cloud Native Development, Duoc UC). Escucha mensajes publicados por el microservicio productor y persiste un resumen de cada inscripción procesada.

## Descripción

Este servicio se suscribe a una cola de RabbitMQ (`resumenInscripcionQueue`) donde el microservicio `PlataformaEducativa` publica el resumen de cada inscripción generada. Al recibir un mensaje, lo transforma y lo almacena en base de datos para su consulta posterior.

## Arquitectura de mensajería

```
PlataformaEducativa (Productor)  →  RabbitMQ (Exchange: resumenInscripcionExchange)
                                          ↓ routing key: resumenInscripcion.routingkey
                                     Queue: resumenInscripcionQueue
                                          ↓
                              resumen-compra-consumer (este servicio)
                                          ↓
                                   Base de datos (H2 / Oracle Cloud)
```

RabbitMQ corre en una instancia EC2 dedicada, independiente de las instancias que alojan los microservicios, para desacoplar el broker de la infraestructura de aplicación.

## Stack técnico

- **Java 21**
- **Spring Boot 3.5.16**
- **Spring AMQP** (`spring-boot-starter-amqp`) — conexión y listener de RabbitMQ
- **Spring Data JPA** — persistencia
- **H2** (temporal, en memoria) — mientras se habilita Oracle Cloud Autonomous Database
- **Oracle JDBC Driver** (`ojdbc11`) — preparado para la migración a Oracle Cloud
- **Lombok**
- **Docker** — build multi-stage
- **GitHub Actions** — CI/CD hacia AWS EC2

## Flujo de procesamiento

1. `PlataformaEducativa` publica un mensaje `ResumenInscripcionMessage` (JSON) al exchange `resumenInscripcionExchange`.
2. RabbitMQ enruta el mensaje a `resumenInscripcionQueue` según la routing key `resumenInscripcion.routingkey`.
3. `ResumenCompraListener` consume el mensaje (`@RabbitListener`), lo convierte a la entidad `ResumenCompra`, y lo persiste vía `ResumenCompraRepository`.
4. Cada registro guarda: ID de inscripción, datos del estudiante, cursos, total a pagar, fecha de inscripción, fecha de procesamiento, y el contenido textual del resumen.

## Variables de entorno

| Variable | Descripción |
|---|---|
| `RABBITMQ_HOST` | Host/IP de la instancia EC2 donde corre el broker RabbitMQ |
| `RABBITMQ_PORT` | Puerto AMQP de RabbitMQ (5672) |
| `RABBITMQ_USERNAME` | Usuario de autenticación contra RabbitMQ |
| `RABBITMQ_PASSWORD` | Contraseña de autenticación contra RabbitMQ |

> Nota: la conexión a base de datos actualmente usa H2 en memoria como solución temporal. Las variables `ORACLE_DB_URL`, `ORACLE_DB_USERNAME` y `ORACLE_DB_PASSWORD` ya están contempladas en `application.properties` (comentadas) para cuando se habilite la instancia de Oracle Cloud Autonomous Database.

## Despliegue

El servicio corre en la misma instancia EC2 que el microservicio productor (`PlataformaEducativa`), en el puerto **8081**, como contenedor Docker independiente.

### Pipeline CI/CD (GitHub Actions)

El workflow (`.github/workflows/main.yml`) se dispara con cada push a `main` y ejecuta:

1. **Checkout** del repositorio.
2. **Build y push** de la imagen Docker a Docker Hub (`resumen-compra:latest`).
3. **Configuración de credenciales AWS** (sesión temporal de AWS Academy).
4. **Despliegue vía SSH** a la instancia EC2:
   - Pull de la imagen más reciente.
   - Detiene y elimina el contenedor `resumen-compra` anterior (si existe).
   - Levanta el contenedor nuevo en el puerto 8081, inyectando las variables de entorno de RabbitMQ.

### Secrets de GitHub Actions requeridos

| Secret | Uso |
|---|---|
| `DOCKERHUB_USERNAME` / `DOCKERHUB_TOKEN` | Autenticación en Docker Hub |
| `AWS_ACCESS_KEY` / `AWS_SECRET_KEY` / `AWS_SESSION_TOKEN` | Credenciales temporales de AWS Academy |
| `EC2_HOST` | IP elástica de la instancia donde se despliega |
| `USER_SERVER` | Usuario SSH de la instancia (`ec2-user`) |
| `EC2_SSH_KEY` | Llave privada SSH para conexión a la instancia |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` / `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | Conexión al broker RabbitMQ |

## Ejecución local

```bash
# Clonar el repositorio
git clone <url-del-repo>
cd resumen-compra-consumer

# Configurar variables de entorno (ver .env.example)
# Levantar con Maven
./mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8081`.

## Estructura del proyecto

```
src/main/java/com/duoc/resumen_compra_consumer/
├── config/
│   └── RabbitMQConfig.java       # Configuración de conexión, exchange, queue y binding
├── dto/
│   └── ResumenInscripcionMessage.java  # Estructura del mensaje recibido
├── listener/
│   └── ResumenCompraListener.java      # Consumidor del mensaje (@RabbitListener)
├── model/
│   └── ResumenCompra.java        # Entidad JPA persistida
└── repository/
    └── ResumenCompraRepository.java   # Acceso a datos (Spring Data JPA)
```

