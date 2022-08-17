package de.rweisleder.example.users.business;

import de.rweisleder.example.users.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceTest {

    @MockBean
    UserRepository repository;

    @Autowired
    UserService userService;

    @Test
    @WithAnonymousUser
    void addUser_anonymously_should_fail() {
        assertThatThrownBy(() -> userService.addUser("John"))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addUser_as_admin_should_save_user() {
        userService.addUser("John");

        verify(repository).addUser(any());
    }

    @Test
    @WithAnonymousUser
    void deleteUserWithName_anonymously_should_fail() {
        assertThatThrownBy(() -> userService.deleteUserWithName("John"))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUserWithName_as_admin_should_load_and_delete_user() {
        userService.deleteUserWithName("John");

        verify(repository).findUserByName(any());
        verify(repository).deleteUser(any());
    }

    @Test
    void deleteAllUsers() {
        userService.deleteAllUsers();

        verify(repository).deleteAllUsers();
    }
}