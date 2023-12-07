# SpringBoot_PricesAPI
## Descripción general
API basada en la prueba de Inditex bajo arquitectura hexagonal, con version Java 17 y Spring 3.2.0, bbdd H2 y OpenAPI para la realización de pruebas y manejo de excepciones custom.
## Notas sobre el proyecto
- He querido centrarme en el endpoint de la prueba bajo las instrucciones de entrada y salida que se pide en la prueba.
- También lo he querido realizar de la manera más genérica posible, para que este mismo endpoint sirviese para realizar
búsquedas con cualquier filtro que el usuario desease asignar.
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
- NOTA: Si se utiliza algún endpoint antes de verificar la prueba para crear o actualizar campos, obviamente los tests fallarán al haber cambiado las rows de la prueba.
1) Para este endpoint (aunque queda explicado en OpenAPI), todos los campos son String, para poder aplicarles ciertos filtros y orderByPriority si se desea filtrar por prioridad o no para mayor flexibilidad al endpoint.
2) Los filtros son los siguientes: eq:, neq:, lt:, gt:, y bw: (este último solo para fechas).
3) Un ejemplo del campo date con formato yyyyMMddHHmmss podría ser eq:20201230235959 (. Este ejemplo aplicará un filtro EQUALS sobre el campo startDate. (yo habría aplicado 2 campos fechas para decidir si queremos comparar startDate, 
endDate o ambos pero he querido realizar la prueba lo más ajustada posible pero haciendola versátil).
4) Otro ejemplo de fecha podría ser bw:20221215000000 que realizará una busqueda entre los campos startDate y endDate.
### Resto de endpoints
- Son opcionales en la prueba pero los he insertado porque forman parte de una API REST. Para los campos fecha siguen el mismo formato yyyyMMddHHmmss.

### Excepciones
- Manejo de excepciones customizadas.


