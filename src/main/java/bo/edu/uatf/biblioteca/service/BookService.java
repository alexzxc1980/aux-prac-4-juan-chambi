package bo.edu.uatf.biblioteca.service;

import bo.edu.uatf.biblioteca.dto.BookRequest;
import bo.edu.uatf.biblioteca.dto.BookResponse;
import bo.edu.uatf.biblioteca.model.Book;
import bo.edu.uatf.biblioteca.repository.BookRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public BookResponse create(BookRequest request) {
        Book book = new Book();
        apply(book, request);
        return BookResponse.from(repository.save(book));
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return repository.findAll(Sort.by("id")).stream().map(BookResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return BookResponse.from(requireBook(id));
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = requireBook(id);
        apply(book, request);
        return BookResponse.from(repository.save(book));
    }

    public void delete(Long id) {
        repository.delete(requireBook(id));
    }

    private Book requireBook(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    private void apply(Book book, BookRequest request) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPublicationYear(request.publicationYear());
        book.setCategory(request.category());
        book.setAvailable(request.available());
    }
}
