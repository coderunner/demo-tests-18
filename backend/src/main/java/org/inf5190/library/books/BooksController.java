package org.inf5190.library.books;

import java.time.Duration;
import java.util.List;
import org.inf5190.library.books.model.Book;
import org.inf5190.library.books.repository.BooksRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;

@RestController
public class BooksController {
    private Timer getTimer = Timer.builder("org.inf5190.library.books.BooksController.get")
            .publishPercentileHistogram(true).maximumExpectedValue(Duration.ofSeconds(10))
            .register(Metrics.globalRegistry);
    private Timer postTimer = Timer.builder("org.inf5190.library.books.BooksController.post")
            .publishPercentiles(new double[] {0.5, 0.95, 0.99}).register(Metrics.globalRegistry);
    private Timer deleteTimer = Timer.builder("org.inf5190.library.books.BooksController.delete")
            .publishPercentileHistogram(true).maximumExpectedValue(Duration.ofSeconds(10))
            .register(Metrics.globalRegistry);

    private final BooksRepository booksRepository;

    public BooksController(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @GetMapping("/books")
    public List<Book> get(@RequestParam(name = "limit", defaultValue = "20") Integer limit,
            @RequestParam(name = "order", defaultValue = "asc") Order order) throws Exception {
        return this.getTimer.recordCallable(() -> {
            return this.booksRepository.getBooks(limit, order);
        });
    }

    @PostMapping("/books")
    public Book add(@RequestBody Book book) throws Exception {
        return this.postTimer.recordCallable(() -> {
            return this.booksRepository.addBook(book);
        });
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) throws Exception {
        return this.deleteTimer.recordCallable(() -> {
            this.booksRepository.deleteBook(id);
            return ResponseEntity.noContent().build();
        });
    }
}
