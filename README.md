# clientes-ms

Microservicio consumidor del servicio **contactos-ms**.

Expone una API REST que permite la comunicación con el servicio `contactos-ms` para dar de alta contactos y realizar consultas sobre los mismos. Forma parte de una arquitectura basada en microservicios.

---

## Responsabilidad

Este microservicio es responsable de:

- Recibir personas para su alta en contactos
- Exponer endpoints REST orientados a cliente
- Realizar llamadas al microservicio `contactos-ms`
- Encapsular la lógica de negocio relacionada con contactos

---

## Tecnologías

- Java 21
- Spring Boot
- Spring Web (REST)
- Maven
- Git / GitHub
- Eclipse IDE

---

## Comunicación entre servicios

La comunicación con el microservicio `contactos-ms` se realiza mediante:

- **RestTemplate**

> Nota: Para esta prueba se utiliza `RestTemplate`. En un entorno productivo se valoraría el uso de OpenFeign o WebClient.

---

## Endpoints

### Alta de persona

- **Método:** `GET`
- **URL:** `/persona/{nombre}/{email}/{edad}`
- **Descripción:** Da de alta una persona en el servicio `contactos-ms` y devuelve la lista completa de personas.

#### Path variables

| Nombre | Tipo   | Descripción |
|-------|--------|-------------|
| nombre | String | Nombre de la persona |
| email  | String | Email de la persona |
| edad   | int    | Edad |

#### Ejemplo

```
GET /persona/Juan/juan@mail.com/30
```

> ⚠️ Nota: Este endpoint utiliza `GET` para realizar un alta únicamente con fines de prueba.

---

### Buscar personas por rango de edad

- **Método:** `GET`
- **URL:** `/persona/{edad1}/{edad2}`
- **Descripción:** Devuelve la lista de personas cuya edad está comprendida entre `edad1` y `edad2` (inclusive).

#### Path variables

| Nombre | Tipo | Descripción |
|-------|------|-------------|
| edad1 | int  | Edad mínima |
| edad2 | int  | Edad máxima |

#### Ejemplo

```
GET /persona/18/30
```

> Nota: El filtrado por edad se realiza en `clientes-ms` tras recuperar la lista completa desde `contactos-ms`.

---

## Ejecución

Para arrancar el microservicio en local:

```bash
mvn spring-boot:run
```

---

## Observaciones

- Este proyecto tiene fines de **aprendizaje y prueba**.
- No se persigue un diseño REST estrictamente purista.
- El foco está en la comunicación entre microservicios y el uso de `RestTemplate`.
