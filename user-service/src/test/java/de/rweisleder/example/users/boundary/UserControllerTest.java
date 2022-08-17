package de.rweisleder.example.users.boundary;

import de.rweisleder.example.users.persistence.UserEntity;
import de.rweisleder.example.users.persistence.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserRepository repository;

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    void getUser_should_return_user_as_json() throws Exception {
        when(repository.findUserByName("John")).thenReturn(new UserEntity(1, "John"));

        mvc.perform(get("/user").queryParam("name", "John"))
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE));
    }

    @Test
    @WithMockUser
    @Disabled("run me for a surprise")
    void getUser_should_return_user_with_specified_json_structure() throws Exception {
        when(repository.findUserByName("John")).thenReturn(new UserEntity(1, "John"));

        mvc.perform(get("/user").queryParam("name", "John"))
                .andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("displayName").value("John"));
    }
}