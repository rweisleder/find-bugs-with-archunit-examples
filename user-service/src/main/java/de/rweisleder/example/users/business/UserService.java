package de.rweisleder.example.users.business;

import de.rweisleder.example.users.persistence.UserEntity;
import de.rweisleder.example.users.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Secured("ROLE_ADMIN")
    public void addUser(String name) {
        log.info("Adding user with name '{}'", name);
        repository.addUser(new UserEntity(name));
    }

    @Secured("ROLE_ADMIN")
    public void deleteUserWithName(String name) {
        log.info("Deleting user with name '{}'", name);
        UserEntity user = repository.findUserByName(name);
        repository.deleteUser(user);
    }

    public void deleteAllUsers() {
        repository.deleteAllUsers();
    }
}
