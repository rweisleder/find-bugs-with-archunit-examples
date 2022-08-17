package de.rweisleder.example.users.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("JpaDataSourceORMInspection")
public class UserEntity {

    private static final String USERS_SEQ_GEN = "USERS_SEQ_GEN";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = SEQUENCE, generator = USERS_SEQ_GEN)
    @SequenceGenerator(name = USERS_SEQ_GEN, sequenceName = "SEQ_USERS")
    private Long id;

    @Column(name = "NAME")
    private String name;

    public UserEntity(String name) {
        this.name = name;
    }

    public UserEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
