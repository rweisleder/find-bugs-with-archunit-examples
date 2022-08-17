package de.rweisleder.example.users.boundary;

import de.rweisleder.example.users.persistence.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("displayName")
    private String name;

    public UserDto(UserEntity user) {
        id = user.getId();
        name = user.getName();
    }
}
