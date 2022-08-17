package de.rweisleder.example.users.business;

import de.rweisleder.example.users.persistence.UserEntity;
import de.rweisleder.example.users.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserNameServiceTest {

    @MockBean
    UserRepository repository;

    @Autowired
    UserNameservice service;

    @BeforeEach
    void setUp() {
        when(repository.findUserById(1L)).thenAnswer(invocation -> {
            Thread.sleep(1000); // slow database ;-)
            return new UserEntity("User 1");
        });
    }

    @Test
    void getDisplayName() {
        // warm up cache
        service.getDisplayName(1L);

        // second invocation should be faster than 1 sec
        assertTimeout(Duration.ofMillis(100), () -> service.getDisplayName(1L));
    }

    @Test
    @Disabled("run me for a surprise")
    void getDisplayNameWithId() {
        // warm up cache
        service.getDisplayName(1L);

        // getDisplayNameWithId should use cache of getDisplayName
        assertTimeout(Duration.ofMillis(100), () -> service.getDisplayNameWithId(1L));
    }
}