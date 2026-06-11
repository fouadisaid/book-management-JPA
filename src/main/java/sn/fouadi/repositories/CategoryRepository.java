package sn.fouadi.repositories;

import jakarta.persistence.EntityManager;

public class CategoryRepository {
    EntityManager em = JpaUtil.getEntityManager();
}
