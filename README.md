## Prices API - Prueba Inditex
### Descripción general
API basada en la prueba de Inditex bajo arquitectura hexagonal, con version Java 17 y Spring 3.2.0, bbdd H2 y OpenAPI para la realización de pruebas y manejo de excepciones.
### Notas sobre el proyecto
- He querido centrarme en el endpoint de la prueba bajo las instrucciones de entrada y salida que se pide en la prueba.
- También lo he querido realizar de la manera más versátil y genérica posible, para que este mismo endpoint sirviese para realizar
búsquedas con cualquier filtro que el usuario desease asignar y no solo lo que pide la prueba.
- He considerado omitir la lógica de servicio de la entity Brand, dado que sería repetir información innecesariamente para la prueba.
### Pautas a seguir
- Hacer git clone https://github.com/fran199017/Prueba_Inditex_API.git
- Abrir el proyecto en la raiz ../pruebainditex
- Hacer mvn clean package
- Ejecutar java -jar target/pruebainditex-0.0.1-SNAPSHOT.jar

### BBDD H2 instrucciones
- URL: http://localhost:8080/h2-console
- url: jdbc:h2:mem:testdb
- username: sa
- password:

### Swagger URL
http://localhost:8080/swagger-ui/index.html

### Endpoint de la prueba
http://localhost:8080/swagger-ui/index.html#/prices-controller/findAll
- NOTA: Si se utiliza alguna otro endpoint, los tests podrían fallar al haber cambiado campos de la prueba.
1) Para este endpoint (aunque queda explicado en OpenAPI), todos los campos son String, para poder aplicarles ciertos filtros y orderByPriority si se desea filtrar por prioridad o no para mayor flexibilidad al endpoint.
2) Los filtros son los siguientes: eq:, neq:, lt:, gt:, y bw: (este último solo para fechas).
3) Un ejemplo del campo date con formato yyyyMMddHHmmss podría ser eq:20201230235959 (. Este ejemplo aplicará un filtro EQUALS sobre el campo startDate. (yo habría aplicado 2 campos fechas para decidir si queremos comparar startDate, 
endDate o ambos pero he querido realizar la prueba lo más ajustada posible pero haciéndola versátil).
4) Otro ejemplo de fecha podría ser bw:20221215000000 que realizará una bésqueda entre los campos startDate y endDate.

### Resto de endpoints
- Son opcionales en la prueba pero los he insertado porque forman parte de una API REST. Para los campos fecha siguen el mismo formato yyyyMMddHHmmss.

### Manejo de excepciones customizadas
- Se puede probar a aplicar un filtro erroneo en el findAll -> Ejemplo ab:yyyyMMddHHmmss
- Se puede probar a no aplicar ningun filtro en el findAll ->  Ejemplo yyyyMMddHHmmss
- Se puede probar a aplicar una fecha "válida" para el @RequestBody pero inválida en conversión -> Ejemplo: eq:20210632000000 (día 32)
- Se puede probar a insertar como brandId (foreign key de Prices) una brandId inexistente -> Ejemplo brandId:5


