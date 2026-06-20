package sn.fouadi.main;

import sn.fouadi.entities.Book;
import sn.fouadi.entities.Category;
import sn.fouadi.repositories.BookRepository;
import sn.fouadi.repositories.CategoryRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        CategoryRepository cr = new CategoryRepository();
        BookRepository     br = new BookRepository();

       //CREATION DES CATEGORIEES
        Category cat1  = cr.create(Category.builder().name("Littérature").build());
        Category cat2 = cr.create(Category.builder().name("Informatique").build());
        Category cat3     = cr.create(Category.builder().name("Sciences").build());

        System.out.println("Categorie 1 " + cat1);
        System.out.println("catrgorie 2 " + cat2);
        System.out.println("categorie 3 " + cat3);

        //Lister tout les categories
        List<Category> toutesLesCategories = cr.getAllCategories2();

        System.out.println("Nombre total : " + toutesLesCategories.size() + " catégorie(s)");
        for (Category c : toutesLesCategories) {
            System.out.println(c);
        }

        //Trouver une categorie par ID
        try {
            int idRecherche = cat1.getId();
            Category trouve = cr.findCategoryById(idRecherche);
            System.out.println("Catégorie trouvée (id=" + idRecherche + ") : " + trouve);
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }


        //CREATE BOOKS
        System.out.println("  CRÉATION DES LIVRES");
        Book livre1 = br.createBook(Book.builder()
                .title("Le Petit Prince")
                .author("Antoine de Saint-Exupéry")
                .publicationYear(1943)
                .countPages(96)
                .category(cat1)
                .build());

        Book livre2 = br.createBook(Book.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .publicationYear(2008)
                .countPages(431)
                .category(cat2)
                .build());

        Book livre3 = br.createBook(Book.builder()
                .title("Design Patterns")
                .author("Gang of Four")
                .publicationYear(1994)
                .countPages(395)
                .category(cat2)
                .build());

        Book livre4 = br.createBook(Book.builder()
                .title("Une Brève Histoire du Temps")
                .author("Stephen Hawking")
                .publicationYear(1988)
                .countPages(212)
                .category(cat3)
                .build());

        Book livre5 = br.createBook(Book.builder()
                .title("L'Étranger")
                .author("Albert Camus")
                .publicationYear(1942)
                .countPages(186)
                .category(cat1)
                .build());

        System.out.println("Book 1" + livre1.getIsbn() + " — " + livre1.getTitle());
        System.out.println("Book 2 " + livre2.getIsbn() + " — " + livre2.getTitle());
        System.out.println("Book 3 " + livre3.getIsbn() + " — " + livre3.getTitle());
        System.out.println("Book 4 " + livre4.getIsbn() + " — " + livre4.getTitle());
        System.out.println("Book 4 " + livre5.getIsbn() + " — " + livre5.getTitle());


        // Lister tous les livres
        System.out.println("  LISTER TOUS LES LIVRES ");
        List<Book> tousLesLivres = br.listAllBooks();
        System.out.println("Total : " + tousLesLivres.size() + " livre(s)");
        for (Book b : tousLesLivres) {
            System.out.println("  → [" + b.getIsbn() + "] " + b.getTitle()
                    + " (" + b.getPublicationYear() + ") — " + b.getAuthor()
                    + " | Catégorie : " + b.getCategory().getName());
        }

        // Trouver un livre par ID
        System.out.println("RECHERCHE PAR ID ");

        try {
            Book trouve = br.findBookById(livre2.getId());
            System.out.println("Trouvé → " + trouve);
        } catch (Exception e) {
            System.out.println("Non trouvé" + e.getMessage());
        }


        // Trouver un livre par ISBN
        System.out.println(" RECHERCHE PAR ISBN ");

        try {
            Book parIsbn = br.findBookByIsbn(livre1.getIsbn());
            System.out.println("Trouvé → " + parIsbn.getTitle() + " | ISBN : " + parIsbn.getIsbn());
        } catch (Exception e) {
            System.out.println("Non trouvé" + e.getMessage());
        }


        // Modifier un livre
        System.out.println(" MODIFICATION D'UN LIVRE");

        System.out.println("Avant : " + br.findBookById(livre3.getId()));

        Book modifs = Book.builder()
                .title("Design Patterns — Solutions réutilisables")
                .author("Gang of Four (GoF)")
                .publicationYear(1994)
                .countPages(420)
                .category(cat2)
                .build();

        br.updateBook(livre3.getId(), modifs);
        System.out.println("Après : " + br.findBookById(livre3.getId()));
        System.out.println("Livre mis à jour.");

        // Livres par catégorie
        System.out.println(" LIVRES PAR CATÉGORIE");

        List<Book> livresInfo = br.listBooksByCategory("informatique");
        System.out.println("Catégorie « Informatique » (" + livresInfo.size() + " livre(s)) :");
        for (Book b : livresInfo) {
            System.out.println("  → " + b.getTitle() + " — " + b.getAuthor());
        }

        List<Book> livresLitt = br.listBooksByCategory("littérature");
        System.out.println("Catégorie « Littérature » (" + livresLitt.size() + " livre(s)) :");
        for (Book b : livresLitt) {
            System.out.println("  → " + b.getTitle() + " — " + b.getAuthor());
        }


        //  On Recherche un livre par titre

        System.out.println(" RECHERCHE PAR TITRE ");

        String motCle = "clean";
        List<Book> parTitre = br.searchBooksByTitle(motCle);
        System.out.println("Résultats pour \"" + motCle + "\" (" + parTitre.size() + " résultat(s)) :");
        for (Book b : parTitre) {
            System.out.println("  → " + b.getTitle());
        }

        // Recherche par auteur
        System.out.println(" RECHERCHE PAR AUTEUR ");

        String auteur = "camus";
        List<Book> parAuteur = br.searchBooksByAuthor(auteur);
        System.out.println("Résultats pour \"" + auteur + "\" (" + parAuteur.size() + " résultat(s)) :");
        for (Book b : parAuteur) {
            System.out.println("  → " + b.getTitle() + " — " + b.getAuthor());
        }

        // Livres après une année
        System.out.println("LIVRES APRÈS UNE ANNÉE");

        int annee = 2000;
        List<Book> apresAnnee = br.searchBooksAfterYear(annee);
        System.out.println("Livres publiés après " + annee + " (" + apresAnnee.size() + " résultat(s)) :");
        for (Book b : apresAnnee) {
            System.out.println("  → " + b.getTitle() + " (" + b.getPublicationYear() + ")");
        }

        // On compte les livres par catégorie
        System.out.println("COMPTAGE PAR CATÉGORIE");

        List<Object[]> stats = br.countBooksByCategory();
        System.out.println("Répartition des livres par catégorie :");
        for (Object[] row : stats) {
            String nomCat  = (String) row[0];
            Long   nbLivres = (Long)   row[1];
            System.out.println("  → " + nomCat + " : " + nbLivres + " livre(s)");
        }

        // Compter tous les livres
        System.out.println("Nombre total de livres en base : " + br.countAllBooks());

        //  Supprimer un livre
        System.out.println("Suppression du livre");
        System.out.println("Suppression de : " + livre5.getTitle());
        br.deleteBook(livre5.getId());
        System.out.println("Supprimé. Total restant : " + br.countAllBooks() + " livre(s)");


        //On verifie un ID inexistant

        try {
            br.findBookById(9999);
        } catch (Exception e) {
            System.out.println(" Exception capturée : " + e.getMessage());
        }

    }
}