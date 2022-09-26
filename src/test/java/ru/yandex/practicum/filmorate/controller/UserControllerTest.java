package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class UserControllerTest {
    @Autowired
    UserController userController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String USERS_PATH = "/users";

    @Test
    public void whenPostRequestToUsers_requestIsEmpty_thenCorrectResponse() throws Exception {
        String emptyUser = "";

        mockMvc.perform(post(USERS_PATH)
                        .content(emptyUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void whenGetRequestToUsers_thenCorrectResponse() throws Exception {
        User user = createTestUser(20, "testEmail@mail.com", "test_login",
                "test_name", LocalDate.of(1991, 8, 20));
        User user2 = createTestUser(29, "test2Email@mail.com", "test_login2",
                "test_name2", LocalDate.of(1993, 2, 12));

        mockMvc.perform(get(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user, user2))));
    }

    @Test
    public void whenPostRequestToUsersAndValidUser_thenCorrectResponse() throws Exception {
        String validUser = "{\"id\": 1, \"email\": \"valid@mail.com\", \"login\": \"valid_Login\"" +
                ", \"name\": \"Simple Name\", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(post(USERS_PATH)
                        .content(validUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(validUser));
    }

    @Test
    public void whenPostRequestToUsersAndInValidUser_incorrectId_thenCorrectResponse() throws Exception {
        String inValidUser = "{\"id\": -1, \"email\": \"valid@mail.com\", \"login\": \"valid_Login\"" +
                ", \"name\": \"Simple Name\", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(post(USERS_PATH)
                        .content(inValidUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Incorrect id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void whenPostRequestToUsersAndInValidLogin_null_thenCorrectResponse() throws Exception {
        String inValidUser = "{\"id\": 2, \"email\": \"valid@mail.com\", \"login\": null" +
                ", \"name\": \"Simple Name\", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(post(USERS_PATH)
                        .content(inValidUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToUsersAndInValidLogin_containsWhiteSpace_thenCorrectResponse() throws Exception {
        String inValidUser = "{\"id\": 3, \"email\": \"valid@mail.com\", \"login\": \"inValid Login\"" +
                ", \"name\": \"Simple Name\", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(post(USERS_PATH)
                        .content(inValidUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Invalid login",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void whenPostRequestToUsersAndInValidUserEmail_empty_thenCorrectResponse() throws Exception {
        String inValidUser = "{\"id\": 4, \"email\": \"\", \"login\": \"valid_Login\"" +
                ", \"name\": \"Simple Name\", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(post(USERS_PATH)
                        .content(inValidUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToUsersAndInValidUserEmail_incorrect_thenCorrectResponse() throws Exception {
        String inValidUser = "{\"id\": 5, \"email\": \"incorrect?@mail,gg\", \"login\": \"valid_Login\"" +
                ", \"name\": \"Simple Name\", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(post(USERS_PATH)
                        .content(inValidUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToUsersAndInValidUserBirthday_thenCorrectResponse() throws Exception {
        String inValidUser = "{\"id\": 6, \"email\": \"valid@mail.com\", \"login\": \"valid_Login\"" +
                ", \"name\": \"Simple Name\", \"birthday\": \"2991-08-20\"}";

        mockMvc.perform(post(USERS_PATH)
                        .content(inValidUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToUsersAndValidUser_emptyName_thenCorrectResponse() throws Exception {
        String validUser = "{\"id\": 7, \"email\": \"valid@mail.com\", \"login\": \"valid_Login\"" +
                ", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(post(USERS_PATH)
                        .content(validUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is("valid_Login")));
    }

    @Test
    public void whenPutRequestToUsersAndValidUser_thenCorrectResponse() throws Exception {
        createTestUser(8, "valid@mail.com", "valid_login",
                "Simple Name", LocalDate.of(1991, 8, 20));
        String validUpdatedUser = "{\"id\": 8, \"email\": \"updated_valid@mail.com\", \"login\": \"updated_login\"" +
                ", \"name\": \"Updated Name\", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(put(USERS_PATH)
                        .content(validUpdatedUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.email").value("updated_valid@mail.com"))
                .andExpect(jsonPath("$.login").value("updated_login"))
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void whenPutRequestToUsersAndInValidUser_thenCorrectResponse() throws Exception {
        createTestUser(9, "valid@mail.com", "valid_login",
                "Simple Name", LocalDate.of(1991, 8, 20));
        String inValidUpdatedUser = "{\"id\": -1, \"email\": \"updated_valid@mail.com\", \"login\": \"updated_login\"" +
                ", \"name\": \"Updated Name\", \"birthday\": \"1991-08-20\"}";

        mockMvc.perform(put(USERS_PATH)
                        .content(inValidUpdatedUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Incorrect id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private User createTestUser(long id, String email, String login, String name, LocalDate birthday)
            throws BadRequestException, NotFoundException {

        User user = User.builder().id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();

        userController.createUser(user);
        return user;
    }
}
