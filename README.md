# SpringBoot_PricesAPI
## Descripción general
API basada en la prueba de Inditex bajo arquitectura hexagonal, con version Java 17 y Spring 3.2.0, bbdd H2 y OpenAPI para la realización de pruebas y manejo de excepciones custom.
## Notas sobre el proyecto
- He querido centrarme en el endpoint demandado bajo las instrucciones de entrada y salida que se pide en la prueba sin añadir 
ningún campo más para que la prueba sea lo más realista posible a lo que se pide.
- También lo he querido realizar de la manera más genérica posible, para que este mismo endpoint sirva para realizar
búsquedas con cualquier filtro que el usuario quiera asignar.
- Para mayor legibilidad yo habría quitado las horas, pero como en bbdd tiene que venir
  con las horas he decidido compararlas también.
## Pautas a seguir

## Instrucciones de uso
### H2 database instructions.
- console_url: http://localhost:8080/h2-console
- url: jdbc:h2:mem:testdb
- username: sa
- password:

### Swagger.
http://localhost:8080/swagger-ui/index.html

### Endpoint de la prueba
http://localhost:8080/swagger-ui/index.html#/prices-controller/findAll
1) Para este endpoint (aunque queda explicado en OpenAPI), todos los campos son String, para poder aplicarles ciertos filtros.
2) Los filtros son los siguientes: eq:, neq:, lt:, gt:, y bw: (este último solo para fechas).
3) Un ejemplo del campo date con formato yyyyMMddHHmmss podría ser eq:20201230235959 (. Este ejemplo aplicará un filtro EQUALS sobre el campo startDate. (yo habría aplicado 2 campos fechas para decidir si queremos comparar uno u 
otro pero he querido realizar la prueba lo más ajustada posible y luego customizarla).
4) Otro ejemplo de fecha podría ser bw:20221215000000 que realizará una busqueda entre los campos startDate y endDate.
### Resto de endpoints
- No me he centrado demasiado en el resto de endpoints de una API REST, porque son bastante más sencillos, no obstante los he realizado de manera opcional.

### Excepciones



