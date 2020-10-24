package jpa.boot.jpaboot.repository;

import jpa.boot.jpaboot.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
//        if (item.getId() == null) {
//            em.persist(item);
//        } else {
//            em.merge(item);
//        }
        em.persist(item);
    }

    public Optional<Item> findOne(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
