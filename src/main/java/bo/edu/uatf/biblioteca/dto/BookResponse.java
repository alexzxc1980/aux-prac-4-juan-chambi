package bo.edu.uatf.biblioteca.dto;

import bo.edu.uatf.biblioteca.model.Book;

public record BookResponse(Long id, String title, String author, String isbn, Integer publicationYear, String category, Boolean available) {
    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublicationYear(), book.getCategory(), book.getAvailable());
    }
}
