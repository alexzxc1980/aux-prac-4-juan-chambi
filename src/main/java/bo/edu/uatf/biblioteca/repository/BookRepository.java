package bo.edu.uatf.biblioteca.repository;

import bo.edu.uatf.biblioteca.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
