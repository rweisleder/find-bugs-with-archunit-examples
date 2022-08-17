package de.rweisleder.example.users.business;

import de.rweisleder.example.users.persistence.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserNameservice {

    private final UserRepository repository;

    public UserNameservice(UserRepository repository) {
        this.repository = repository;
    }

    @Cacheable("userDisplayName") // because of slow database ...
    public String getDisplayName(long userId) {
        return repository.findUserById(userId).getName();
    }

    public String getDisplayNameWithId(long userId) {
        return userId + ":" + getDisplayName(userId);
    }
}
