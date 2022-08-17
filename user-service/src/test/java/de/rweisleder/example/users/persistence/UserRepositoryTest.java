package de.rweisleder.example.users.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.NoResultException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(UserRepository.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate db;

    @Test
    void addUser() {
        userRepository.addUser(new UserEntity("John"));
    }

    @Test
    @Sql("user.sql")
    void findUserById() {
        UserEntity user = userRepository.findUserById(1L);

        assertThat(user).isNotNull()
                .extracting(UserEntity::getName).isEqualTo("John");
    }

    @Test
    @Sql("user.sql")
    void findUserByName() {
        UserEntity user = userRepository.findUserByName("John");

        assertThat(user).isNotNull()
                .extracting(UserEntity::getName).isEqualTo("John");
    }

    @Test
    @Sql("user.sql")
    void deleteUser() {
        UserEntity user = userRepository.findUserByName("John");

        userRepository.deleteUser(user);

        assertThatThrownBy(() -> userRepository.findUserByName("John")).isInstanceOf(NoResultException.class);
    }

    @Test
    @Sql("user.sql")
    void deleteAllUsers() {
        userRepository.deleteAllUsers();

        assertThatThrownBy(() -> userRepository.findUserByName("John")).isInstanceOf(NoResultException.class);
    }
}