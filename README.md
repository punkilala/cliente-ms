# clientes-ms

Microservicio consumidor de servico contactos-ms

Expone una API REST que permite la comunicación con el servicio contactos-ms para dar de alta contactos
Forma parte de una arquitectura basada en microservicios.

## Responsabilidad

Este microservicio es responsable de:

- Recibir personas para su alta en contactos
- Exposición de endpoints REST
- llamadas a contactos-ms
- Encapsular la lógica de negocio relacionada con contactos

## Tecnologías

- Java 21
- Spring Boot
- Spring Web (REST)
- Maven
- Git / GitHub
- Eclipse IDE

## Tipos llamadas
En un principio se hace una prueba con RestTemplate pero se actualizara WebClient por estar este primero deprecated


Proyecto Spring Boot estándar.

Desde la raíz del proyecto:

```bash
mvn spring-boot:run
