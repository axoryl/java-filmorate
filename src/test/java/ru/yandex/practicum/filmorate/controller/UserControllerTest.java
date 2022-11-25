package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidUser;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static final String BASE_URI = "/users";

    @Test
    void whenGetRequestGetAllUsers_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetUserById_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/" + 1))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetUserFriends_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/" + 1 + "/friends"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetMutualFriends_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/" + 1 + "/friends/common/" + 2))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestIsEmpty_thenCorrectResponse() throws Exception {
        mockMvc.perform(post(BASE_URI))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void whenPostRequestSave_thenCorrectResponse() throws Exception {
        final var user = getValidUser();
        when(userService.save(user)).thenReturn(user);

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestSaveAndInValidUser_incorrectId_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setId(-1L);

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestSaveAndInValidLogin_null_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setLogin(null);

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestSaveAndInValidLogin_containsWhiteSpace_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setLogin("User Login");

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestSaveAndInValidUserEmail_empty_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setEmail("");

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidUserEmail_incorrect_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setEmail("incorrect@?mail><.com");

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestSaveAndInValidUserBirthday_thenCorrectResponse() throws Exception {
        final var inValidUser = getValidUser();
        inValidUser.setBirthday(LocalDate.now().plusDays(1));

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndValidUser_emptyName_thenCorrectResponse() throws Exception {
        final var validUser = getValidUser();
        validUser.setName("");

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(validUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutRequestUpdate_thenCorrectResponse() throws Exception {
        final var user = getValidUser();
        when(userService.update(user)).thenReturn(user);

        mockMvc.perform(put(BASE_URI)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutRequestAddFriend_thenCorrectResponse() throws Exception {
        mockMvc.perform(put(BASE_URI + "/" + 1 + "/friends/" + 2))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteRequestDeleteFriend() throws Exception {
        mockMvc.perform(delete(BASE_URI + "/" + 1 + "/friends/" + 2))
                .andExpect(status().isOk());
    }
}