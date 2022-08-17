package de.rweisleder.example.users.boundary;

import de.rweisleder.example.users.persistence.UserEntity;
import de.rweisleder.example.users.persistence.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public UserDto getUser(@RequestParam("name") String name) {
        try {
            UserEntity user = repository.findUserByName(name);
            return new UserDto(user);
        } catch (Exception e) {
            e.printStackTrace(); // TODO
            return null;
        }
    }
}
