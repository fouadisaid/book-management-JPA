package sn.fouadi.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import sn.fouadi.entities.Book;
import sn.fouadi.entities.Category;

import java.util.List;
import java.util.Random;

public class BookRepository {

    EntityManager em = JpaUtil.getEntityManager();


    //  CREATE
    public Book createBook(Book book) {
        Book newBook = new Book();
        newBook.setIsbn(generateIsbn());                  // ISBN généré automatiquement
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        newBook.setPublicationYear(book.getPublicationYear());
        newBook.setCountPages(book.getCountPages());
        newBook.setCategory(book.getCategory());
        newBook.setUserCreated("admin");
        newBook.setUserUpdated("admin");

        executeInTransaction(() -> em.persist(newBook));
        return newBook;
    }

    // Lister tous les livres
    public List<Book> listAllBooks() {
        return em.createQuery(
                "SELECT b FROM Book b ORDER BY b.title",
                Book.class
        ).getResultList();
    }

    // Trouver un livre par son ID
    public Book findBookById(int id) {
        Book b = em.find(Book.class, id);
        if (b == null) throw new EntityNotFoundException("Livre introuvable : id = " + id);
        return b;
    }

    // Trouver un livre par son ISBN
    public Book findBookByIsbn(String isbn) {
        List<Book> result = em.createQuery(
                "SELECT b FROM Book b WHERE b.isbn = :isbn",
                Book.class
        ).setParameter("isbn", isbn).getResultList();

        if (result.isEmpty()) throw new EntityNotFoundException("Livre introuvable : isbn = " + isbn);
        return result.get(0);
    }

    // Lister les livres d'une catégorie donnée
    public List<Book> listBooksByCategory(String categoryName) {
        return em.createQuery(
                "SELECT b FROM Book b WHERE LOWER(b.category.name) = :catName ORDER BY b.title",
                Book.class
        ).setParameter("catName", categoryName.toLowerCase()).getResultList();
    }

    // Rechercher des livres par mot-clé dans le titre
    public List<Book> searchBooksByTitle(String keyword) {
        return em.createQuery(
                "SELECT b FROM Book b WHERE LOWER(b.title) LIKE :kw ORDER BY b.title",
                Book.class
        ).setParameter("kw", "%" + keyword.toLowerCase() + "%").getResultList();
    }

    // Rechercher des livres par auteur
    public List<Book> searchBooksByAuthor(String keyword) {
        return em.createQuery(
                "SELECT b FROM Book b WHERE LOWER(b.author) LIKE :kw ORDER BY b.author",
                Book.class
        ).setParameter("kw", "%" + keyword.toLowerCase() + "%").getResultList();
    }

    // Lister les livres publiés après une année donnée
    public List<Book> searchBooksAfterYear(int year) {
        return em.createQuery(
                "SELECT b FROM Book b WHERE b.publicationYear > :year ORDER BY b.publicationYear ASC",
                Book.class
        ).setParameter("year", year).getResultList();
    }

    // Compter les livres par catégorie
    public List<Object[]> countBooksByCategory() {
        return em.createQuery(
                "SELECT b.category.name, COUNT(b) FROM Book b GROUP BY b.category.name ORDER BY b.category.name",
                Object[].class
        ).getResultList();
    }

    // Compter tous les livres
    public long countAllBooks() {
        return em.createQuery(
                "SELECT COUNT(b) FROM Book b",
                Long.class
        ).getSingleResult();
    }


    //  UPDATE
    public void updateBook(int id, Book newBook) {
        executeInTransaction(() -> {
            Book b = findBookById(id);
            if (b != null) {
                b.setTitle(newBook.getTitle());
                b.setAuthor(newBook.getAuthor());
                b.setPublicationYear(newBook.getPublicationYear());
                b.setCountPages(newBook.getCountPages());
                b.setCategory(newBook.getCategory());
                b.setUserUpdated("admin");
                // L'ISBN n'est jamais modifié — identifiant métier stable
            }
        });
    }


    //  DELETE
    public void deleteBook(int id) {
        executeInTransaction(() -> {
            Book b = findBookById(id);
            if (b != null) em.remove(b);
        });
    }


    //  GÉNÉRATION ISBN-13
    private String generateIsbn() {
        String[] prefixes = {"978", "979"};
        Random random = new Random();
        String prefix    = prefixes[random.nextInt(2)];
        String group     = String.valueOf(random.nextInt(2));
        String publisher = String.format("%04d", random.nextInt(10000));
        String title     = String.format("%04d", random.nextInt(10000));

        String base      = prefix + group + publisher + title;   // 12 chiffres
        int checkDigit   = computeIsbn13CheckDigit(base);

        return String.format("%s-%s-%s-%s-%d", prefix, group, publisher, title, checkDigit);
    }

    private int computeIsbn13CheckDigit(String base12) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(base12.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int remainder = sum % 10;
        return remainder == 0 ? 0 : 10 - remainder;
    }


    //  GESTION DES TRANSACTIONS
    public void executeInTransaction(Runnable action) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.run();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Erreur transaction : " + e.getMessage(), e);
        }
    }
}