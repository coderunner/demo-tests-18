package org.inf5190.library.books.repository;

public class FirestoreBook {
    private String title;
    private String author;
    private String description;
    private int nbPages;

    public FirestoreBook() {}

    public FirestoreBook(String title, String author, String description, int nbPages) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.nbPages = nbPages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

}
