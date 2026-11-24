package bo.edu.uatf.biblioteca;

import static org.assertj.core.api.Assertions.assertThat;
import bo.edu.uatf.biblioteca.dto.BookRequest;
import bo.edu.uatf.biblioteca.dto.BookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookApiTest {
    @Autowired
    private TestRestTemplate http;

    @Test
    void completeCrudAndMissingResources() {
        var request = new BookRequest("Cien anos de soledad", "Gabriel Garcia Marquez", "9780307474728", 1967, "Novela", true);
        var created = http.postForEntity("/api/books", request, BookResponse.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody()).isNotNull();
        Long id = created.getBody().id();
        String path = "/api/books/" + id;
        assertThat(created.getHeaders().getLocation()).hasToString(path);
        assertThat(http.getForEntity(path, BookResponse.class).getBody()).isEqualTo(created.getBody());
        assertThat(http.getForEntity("/api/books", BookResponse[].class).getBody()).extracting(BookResponse::id).contains(id);
        var changed = new BookRequest("El amor en los tiempos del colera", request.author(), "9780307389732", 1985, request.category(), false);
        var updated = http.exchange(path, HttpMethod.PUT, new HttpEntity<>(changed), BookResponse.class);
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated.getBody().title()).isEqualTo(changed.title());
        assertThat(updated.getBody().available()).isFalse();
        assertThat(http.exchange(path, HttpMethod.DELETE, HttpEntity.EMPTY, String.class).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(http.getForEntity(path, String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(http.exchange(path, HttpMethod.PUT, new HttpEntity<>(request), String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(http.exchange(path, HttpMethod.DELETE, HttpEntity.EMPTY, String.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void rejectsInvalidRequestsAndPublishesOpenApi() {
        var invalid = new BookRequest("", "", "", -1, "", null);
        assertThat(http.postForEntity("/api/books", invalid, String.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        var specification = http.getForEntity("/v3/api-docs", String.class);
        assertThat(specification.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(specification.getBody()).contains("/api/books", "publicationYear", "BookRequest");
        assertThat(http.getForEntity("/swagger-ui/index.html", String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
