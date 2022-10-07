package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.service.user_service.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidUser;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(UserController.class)
@Import({UserServiceImpl.class, InMemoryUserStorage.class})
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USERS_PATH = "/users";

    @Test
    void whenPostRequestIsEmpty_thenCorrectResponse() throws Exception {
        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(1)
    void whenGetRequestGetAllUsers_thenCorrectResponse() throws Exception {
        final var user = getValidUser();
        final var user2 = getValidUser();
        userController.createUser(user);
        userController.createUser(user2);

        mockMvc.perform(get(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user, user2))));
    }

    @Test
    @Order(2)
    void whenPostRequestCreateAndValidUser_thenCorrectResponse() throws Exception {
        final var validUser = getValidUser();
        validUser.setId(3L);

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(validUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(validUser)));
    }

    @Test
    void whenPostRequestCreateAndUserExists_thenCorrectResponse() throws Exception {
        final var user = getValidUser();
        userController.createUser(user);

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("User already exists",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void whenPostRequestCreateAndInValidUser_incorrectId_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setId(-1L);

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestCreateAndInValidLogin_null_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setLogin(null);

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidLogin_containsWhiteSpace_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setLogin("User Login");

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidUserEmail_empty_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setEmail("");

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidUserEmail_incorrect_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setEmail("incorrect@?mail><.com");

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidUserBirthday_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setBirthday(LocalDate.now().plusDays(1));

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndValidUser_emptyName_thenCorrectResponse() throws Exception {
        final var validUser = getValidUser();
        final String login = validUser.getLogin();
        validUser.setName("");

        mockMvc.perform(post(USERS_PATH)
                        .content(objectMapper.writeValueAsString(validUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is(login)));
    }

    @Test
    void whenPutRequestUpdateAndValidUser_thenCorrectResponse() throws Exception {
        final var user = userController.createUser(getValidUser());
        final var updatedUser = getValidUser();
        updatedUser.setId(user.getId());
        updatedUser.setLogin("UpdatedLogin");
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@mail.com");
        updatedUser.setBirthday(LocalDate.of(2000, 1, 1));

        mockMvc.perform(put(USERS_PATH)
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value("updated@mail.com"))
                .andExpect(jsonPath("$.login").value("UpdatedLogin"))
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void whenGetRequestGetUserById_thenCorrectResponse() throws Exception {
        final var user = userController.createUser(getValidUser());

        mockMvc.perform(get(USERS_PATH + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void whenGetRequestGetUserFriends_thenCorrectResponse() throws Exception {
        final var user = userController.createUser(getValidUser());

        mockMvc.perform(get(USERS_PATH + "/" + user.getId() + "/friends")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetUserFriends_userNotExists_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(USERS_PATH + "/" + 9999 + "/friends")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetRequestGetMutualFriends_thenCorrectResponse() throws Exception {
        final var user = userController.createUser(getValidUser());
        final var user2 = userController.createUser(getValidUser());

        mockMvc.perform(get(USERS_PATH + "/" + user.getId() + "/friends/common/" + user2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetMutualFriends_userNotExists_thenCorrectResponse() throws Exception {
        final var user2 = userController.createUser(getValidUser());

        mockMvc.perform(get(USERS_PATH + "/" + 8888 + "/friends/common/" + user2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetRequestGetMutualFriends_user2NotExists_thenCorrectResponse() throws Exception {
        final var user = userController.createUser(getValidUser());

        mockMvc.perform(get(USERS_PATH + "/" + user.getId() + "/friends/common/" + 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPutRequestAddFriend_thenCorrectResponse() throws Exception {
        final var user = userController.createUser(getValidUser());
        final var user2 = userController.createUser(getValidUser());

        mockMvc.perform(put(USERS_PATH + "/" + user.getId() + "/friends/" + user2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
    }

    @Test
    void whenPutRequestAddFriend_userNotExists_thenCorrectResponse() throws Exception {
        final var user2 = userController.createUser(getValidUser());

        mockMvc.perform(put(USERS_PATH + "/" + 9999 + "/friends/" + user2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPutRequestAddFriend_user2NotExists_thenCorrectResponse() throws Exception {
        final var user = userController.createUser(getValidUser());

        mockMvc.perform(put(USERS_PATH + "/" + user.getId() + "/friends/" + 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteRequestDeleteFriend() throws Exception {
        final var user = userController.createUser(getValidUser());
        final var user2 = userController.createUser(getValidUser());
        user.getFriends().add(user2.getId());
        user2.getFriends().add(user.getId());

        mockMvc.perform(delete(USERS_PATH + "/" + user.getId() + "/friends/" + user2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, user.getFriends().size());
        assertEquals(0, user2.getFriends().size());
    }
}
