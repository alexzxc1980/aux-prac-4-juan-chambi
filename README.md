# Práctica 4: Biblioteca

Repositorio: https://github.com/alexzxc1980/aux-prac-4-juan-chambi

Estudiante: Juan Alex Chambi Montes. SIS-414 G2, auxiliar Manuel Martinez Orcko.

API REST de libros con persistencia en PostgreSQL mediante Spring Data JPA y migraciones con Flyway. Se utiliza Java 21, Spring Boot 3.5.16 y Swagger/OpenAPI.

## Ejecutar

Requisitos: JDK 21 y acceso a internet para descargar Maven y las dependencias.

```bash
git clone https://github.com/alexzxc1980/aux-prac-4-juan-chambi.git
cd aux-prac-4-juan-chambi
```

Crear una base de datos PostgreSQL vacía y configurar estas variables en la terminal antes de iniciar:

| Variable | Contenido |
| --- | --- |
| `DB_URL` | URL JDBC, por ejemplo `jdbc:postgresql://localhost:5432/biblioteca` |
| `DB_USERNAME` | Usuario de la base de datos |
| `DB_PASSWORD` | Contraseña del usuario |
| `PORT` | Puerto HTTP opcional; por defecto 8080 |

En PowerShell se asignan con `$env:DB_URL='jdbc:postgresql://localhost:5432/biblioteca'`; configurar del mismo modo usuario y contraseña. En Linux/macOS se usa `export DB_URL='jdbc:postgresql://localhost:5432/biblioteca'`. No subir los valores privados a GitHub. Flyway crea la tabla `books` y registra la migración; Hibernate valida la estructura.

```bash
./mvnw spring-boot:run
```

En Windows: `.\mvnw.cmd spring-boot:run`. Las pruebas se ejecutan con `./mvnw test` o `.\mvnw.cmd test`.

## Endpoints

| Método | Ruta | Resultado |
| --- | --- | --- |
| POST | `/api/books` | Registra un libro y responde 201 con su ubicación |
| GET | `/api/books` | Lista todos los libros; responde 200 |
| GET | `/api/books/{id}` | Consulta un libro; responde 200 o 404 |
| PUT | `/api/books/{id}` | Reemplaza los datos del libro; responde 200 o 404 |
| DELETE | `/api/books/{id}` | Elimina un libro; responde 204 o 404 |

Los cuerpos de POST y PUT llevan `title`, `author`, `isbn`, `publicationYear`, `category` y `available`. El servidor genera `id`. Las solicitudes inválidas devuelven 400.

## Swagger

Abrir http://localhost:8080/swagger-ui/index.html. La especificación JSON está en http://localhost:8080/v3/api-docs. Los ejemplos se pueden ejecutar con **Try it out**.

## Organización

`controller` recibe las solicitudes HTTP; `dto` define los datos de entrada y salida; `service` implementa el CRUD; `model` representa el libro.

`repository` accede a PostgreSQL; `db/migration` conserva la evolución del esquema. Los libros permanecen después de reiniciar el backend. Para ejecutar las pruebas, usar una base PostgreSQL de desarrollo con las mismas variables.

## Render

Backend publicado: https://aux-prac-4-juan-chambi.onrender.com/api/books

Swagger público: https://aux-prac-4-juan-chambi.onrender.com/swagger-ui/index.html

`render.yaml` configura el Web Service Docker y PostgreSQL en Ohio. Las variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME` y `DB_PASSWORD` se obtienen de la base mediante `fromDatabase`. El servicio lee el puerto que Render entrega en `PORT`.

La ruta de comprobación del servicio es `/api/books`. El Dockerfile compila y ejecuta la aplicación; no contiene credenciales.
