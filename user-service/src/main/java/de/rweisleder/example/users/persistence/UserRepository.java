package de.rweisleder.example.users.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRepository {

    private static final Logger log = LogManager.getLogger(UserRepository.class);

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addUser(UserEntity userEntity) {
        log.debug("Adding user ...");
        entityManager.merge(userEntity);
    }

    public UserEntity findUserById(long userId) {
        return entityManager.find(UserEntity.class, userId);
    }

    public UserEntity findUserByName(String name) {
        return entityManager
                .createQuery("SELECT u FROM UserEntity u WHERE u.name = :name", UserEntity.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public void deleteUser(UserEntity userEntity) {
        entityManager.remove(userEntity);
    }

    public void deleteAllUsers() {
        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();
    }
}
