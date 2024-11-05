package org.inf5190.library.books.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.inf5190.library.books.Order;
import org.inf5190.library.books.model.Book;
import org.springframework.stereotype.Repository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.Query.Direction;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

@Repository
public class BooksRepository {
    private List<Book> cache = new ArrayList<>();
    private Firestore firestore;

    public BooksRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    // Ajout du mot clé synchronized pour illustrer la contention
    public synchronized List<Book> getBooks(Integer requestedLimit, Order order)
            throws InterruptedException, ExecutionException {

        final Query query = this.firestore.collection("books");
        final Integer limit = requestedLimit != null ? requestedLimit : 20;
        final Direction direction = order == Order.asc ? Direction.ASCENDING : Direction.DESCENDING;
        final QuerySnapshot snapshot = query.orderBy("title", direction).limit(limit).get().get();

        List<Book> books = snapshot.getDocuments().stream().map(d -> {
            FirestoreBook book = d.toObject(FirestoreBook.class);
            return new Book(d.getId(), book.getTitle(), book.getAuthor(), book.getDescription(),
                    book.getNbPages());
        }).toList();
        cacheResults(books);

        return books;
    }

    public Book addBook(Book book) throws InterruptedException, ExecutionException {
        ApiFuture<DocumentReference> future =
                this.firestore.collection("books").add(new FirestoreBook(book.getTitle(),
                        book.getAuthor(), book.getDescription(), book.getNbPages()));

        DocumentReference doc = future.get();
        return new Book(doc.getId(), book.getTitle(), book.getAuthor(), book.getDescription(),
                book.getNbPages());
    }

    public void deleteBook(String id) throws InterruptedException, ExecutionException {
        ApiFuture<WriteResult> future = this.firestore.collection("books").document(id).delete();
        future.get();
    }

    private void cacheResults(List<Book> books) {
        // pour illustrer un leak
        this.cache.addAll(books);
    }

}
