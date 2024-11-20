
package org.inf5190.library.books;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.inf5190.library.books.model.Book;
import org.inf5190.library.books.repository.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestBooksController {
    private static final Book TEST_BOOK = new Book("id", "title", "author", "desc", 122);

    /**
     * Fourni un mock pour le bean demandé. Nous pourrons ensuite définir le comportement du mock de
     * façon programmatique.
     */
    @Mock
    private BooksRepository mockRepository;

    private BooksController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // On crée le controller à tester en lui passant un mock pour le repo
        this.controller = new BooksController(mockRepository);
    }

    /**
     * Annotation qui indique que la méthode est un test à exécuter.
     */
    @Test
    public void getBooks() throws Exception {
        // Définition de ce qui est attendu.
        final List<Book> expectedBooks = new ArrayList<Book>();
        expectedBooks.add(TEST_BOOK);

        // On définit le comportement du mock.
        when(this.mockRepository.getBooks(20, Order.asc)).thenReturn(expectedBooks);

        // On exécute le test.
        final List<Book> actualBooks = controller.get(20, Order.asc);

        // On valide le résultat.
        assertThat(actualBooks).isEqualTo(expectedBooks);

        // Optionel: on valide que le mock a été appelé avec les bons paramètres.
        verify(this.mockRepository, times(1)).getBooks(20, Order.asc);
    }

    @Test
    public void addBook() throws Exception {
        final Book book = new Book(null, TEST_BOOK.getTitle(), TEST_BOOK.getAuthor(),
                TEST_BOOK.getDescription(), TEST_BOOK.getNbPages());

        when(this.mockRepository.addBook(book)).thenReturn(TEST_BOOK);

        final Book actualBook = controller.add(book);

        assertThat(actualBook).isEqualTo(TEST_BOOK);
        verify(this.mockRepository, times(1)).addBook(book);
    }
}
