package smu.poodle.smnavi.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import smu.poodle.smnavi.domain.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public List<User> findAll(){
        List<User> users = em.createQuery("select u from User as u", User.class).getResultList();
        return users;
    }

    public User findOne(int id){
        User user = em.find(User.class, id);
        return user;
    }

    public void delete(User user){
        em.remove(user);
    }


}
