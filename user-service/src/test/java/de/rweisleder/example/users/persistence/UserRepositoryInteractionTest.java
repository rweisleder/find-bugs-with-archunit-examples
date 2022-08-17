package de.rweisleder.example.users.persistence;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;

@DataJpaTest(properties = {
        "spring.jpa.generate-ddl=false",
        "logging.level.org.hibernate.SQL=DEBUG",
        "logging.level.org.hibernate.type.descriptor.sql=TRACE",
        "logging.level.org.springframework.jdbc.core.JdbcTemplate=DEBUG"
}, showSql = false)
@Import(UserRepository.class)
@Sql("create_structure.sql")
@SuppressWarnings("SqlDialectInspection")
@Disabled("run me for a surprise")
class UserRepositoryInteractionTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    JdbcTemplate db;

    @Test
    void mix_jpa_and_sql() {
        userRepository.addUser(new UserEntity("John"));
        entityManager.flush();

        db.execute("INSERT INTO users (id, name) VALUES (next value for seq_users, 'Mary')");

        userRepository.addUser(new UserEntity("Joe"));
        entityManager.flush();

        System.out.println(db.queryForList("SELECT * FROM users ORDER BY id"));
    }
}
