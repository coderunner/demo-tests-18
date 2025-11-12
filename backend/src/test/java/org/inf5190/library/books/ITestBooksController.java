
package org.inf5190.library.books;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.inf5190.library.books.model.Book;
import org.inf5190.library.books.repository.FirestoreBook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

/**
 * On indique à Spring d'exécuter le test dans le conteneur web sur un port aléatoire.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@PropertySource("classpath:firebase.properties")
public class ITestBooksController {
    private static final Book TEST_BOOK = new Book("id", "title", "author", "desc", 122);

    /**
     * Spring nous injecte le nom du projet firebase, le port de l'émulateur et le port du serveur.
     * 
     * Il nous injecte aussi un client http (TestRestTemplate) et un pointeur vers Firestore.
     */
    @Value("${firebase.project.id}")
    private String firebaseProjectId;

    @Value("${firebase.emulator.port}")
    private String emulatorPort;

    @LocalServerPort
    private int port;

    /**
     * '@Autowired' indique à Spring d'assigner les Beans ici après la création du l'objet de test.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Firestore firestore;

    private String baseMessageUrl;

    /**
     * Méthode appelée avant chaque test.
     * 
     * Elle insert des données spécifique dans firestore (émulateur).
     */
    @BeforeEach
    public void setup() throws InterruptedException, ExecutionException {
        this.baseMessageUrl = String.format("http://localhost:%d/books", port);

        ApiFuture<WriteResult> future1 = this.firestore.collection("books").document()
                .create(new FirestoreBook("test1", "test1", "test1", 111));
        future1.get();
        ApiFuture<WriteResult> future2 = this.firestore.collection("books").document()
                .create(new FirestoreBook("test2", "test2", "test2", 222));
        future2.get();
    }

    /**
     * Méthode appelée après chaque test (qu'il passe ou échoue).
     * 
     * Elle nettoie les données de tests dans l'émulateur.
     */
    @AfterEach
    public void testDown() {
        restTemplate.delete("http://localhost:" + this.emulatorPort + "/emulator/v1/projects/"
                + this.firebaseProjectId + "/databases/(default)/documents");
    }

    @Test
    public void getAllBooks() {
        final String url =
                UriComponentsBuilder.fromUriString(this.baseMessageUrl).build().toUriString();

        // getForObject pour recevoir directement un objet
        final Book[] books = restTemplate.getForObject(url, Book[].class);
        assertThat(books.length).isEqualTo(2);
        assertThat(books[0].getTitle()).isEqualTo("test1");
        assertThat(books[1].getTitle()).isEqualTo("test2");
    }

    @Test
    public void getBooksAsc() {
        final String url = UriComponentsBuilder.fromUriString(this.baseMessageUrl)
                .queryParam("order", "asc").build().toUriString();

        // getForEntity pour recevoir la réponse et valider le code de retour ou prendre
        // de l'information des headers
        final ResponseEntity<Book[]> response = restTemplate.getForEntity(url, Book[].class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        // exemple pour les headers
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

        Book[] books = response.getBody();
        assertThat(books.length).isEqualTo(2);
        assertThat(books[0].getTitle()).isEqualTo("test1");
        assertThat(books[1].getTitle()).isEqualTo("test2");
    }

    @Test
    public void getBooksDesc() {
        final String url = UriComponentsBuilder.fromUriString(this.baseMessageUrl)
                .queryParam("order", "desc").build().toUriString();

        final RequestEntity<Void> request = RequestEntity.get(url)
                // .header("sid", "sessionId") <- exemple de header
                .build();

        // exchange est l'option la plus flexible et permer de passer des headers
        final ResponseEntity<Book[]> response = restTemplate.exchange(request, Book[].class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        Book[] books = response.getBody();
        assertThat(books.length).isEqualTo(2);
        assertThat(books[0].getTitle()).isEqualTo("test2");
        assertThat(books[1].getTitle()).isEqualTo("test1");
    }

    @Test
    public void getBooksWithLimit() {
        final String url = UriComponentsBuilder.fromUriString(this.baseMessageUrl)
                .queryParam("limit", "1").build().toUriString();
        final Book[] books = restTemplate.getForObject(url, Book[].class);
        assertThat(books.length).isEqualTo(1);
    }

    @Test
    public void addBook() {
        restTemplate.postForObject(this.baseMessageUrl, TEST_BOOK, Book.class);

        final Book[] books = this.getBooks();
        final List<Book> bookList = Arrays.asList(books);

        assertThat(books.length).isEqualTo(3);
        assertThat(bookList.stream().filter(b -> b.getTitle().equals(TEST_BOOK.getTitle())).toList()
                .size()).isEqualTo(1);
    }

    @Test
    public void deleteBook() {
        Book[] books = this.getBooks();
        assertThat(books.length).isEqualTo(2);

        final String toDeleteId = books[0].getId();
        restTemplate.delete(this.baseMessageUrl + "/{id}", toDeleteId);

        books = this.getBooks();
        final List<Book> bookList = Arrays.asList(books);

        assertThat(books.length).isEqualTo(1);
        assertThat(bookList.stream().anyMatch(b -> b.getId() == toDeleteId)).isFalse();
    }

    private Book[] getBooks() {
        return restTemplate.getForObject(this.baseMessageUrl, Book[].class);
    }
}
