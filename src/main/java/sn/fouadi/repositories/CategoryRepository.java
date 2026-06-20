package sn.fouadi.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import sn.fouadi.entities.Category;

import java.util.List;

public class CategoryRepository {
    EntityManager em = JpaUtil.getEntityManager();

    public Category create(Category cat)
    {

        Category category = new Category();
        category.setName(cat.getName());
        category.setState(Boolean.TRUE);
        category.setUserCreated("admin");
        category.setUserUpdated("admin");


       executeInTransaction(() -> em.persist(category));
       return category;
    }

    //Lister tout les categories

    //Première methode
    /* public List<Category> getAllCategories1() {
        return em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class).getResultList();
    }*/

    //deuxième Methode
    public List<Category> getAllCategories2( ) {
        return em.createNamedQuery("Category.All", Category.class).getResultList();

    }

    public Category findCategoryById(int id) {
        Category c = em.find(Category.class, id);

        if (c == null) throw new EntityNotFoundException("Category introuvable : " + id);
        return c;
    }

    //Modifie une categorie
    public void updateCategory(int id, Category newCategory) {
        executeInTransaction(() -> {
            Category c = findCategoryById(id);
            if (c != null) {
                c.setName(newCategory.getName());
                c.setState(newCategory.isState());
                c.setUserUpdated("admin");
            }
        });
    }

    //Suppression d'une category
    public void deleteCategory(int id) {
        executeInTransaction(() -> {
            Category c = findCategoryById(id);
            if (c != null) em.remove(c);
        });
    }

    //Recherche une categorie par son nom
    public List<Category> searchCategoriesByName(String keyword) {
        return em.createQuery(
                        "SELECT c FROM Category c WHERE LOWER(c.name) LIKE :kw ORDER BY c.name",
                        Category.class)
                .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                .getResultList();
    }

    //categories dont leurs statut = TRUE
    public List<Category> listActiveCategories() {
        return em.createQuery(
                        "SELECT c FROM Category c WHERE c.state = true ORDER BY c.name",
                        Category.class)
                .getResultList();
    }

    //Compter tout les categories
    public long countAllCategories() {
        return em.createQuery("SELECT COUNT(c) FROM Category c", Long.class)
                .getSingleResult();
    }


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
