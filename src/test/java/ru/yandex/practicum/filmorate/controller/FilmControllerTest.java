package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.service.film_service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.user_service.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidFilm;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidUser;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest({FilmController.class, UserController.class})
@Import({FilmServiceImpl.class, UserServiceImpl.class, InMemoryFilmStorage.class, InMemoryUserStorage.class})
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmController filmController;

    @Autowired
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String FILMS_PATH = "/films";

    @Test
    void whenPostRequestIsEmpty_thenCorrectResponse() throws Exception {
        mockMvc.perform(post(FILMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(1)
    void whenGetRequestGetAllFilms_thenCorrectResponse() throws Exception {
        final var film = getValidFilm();
        final var film2 = getValidFilm();
        filmController.createFilm(film);
        filmController.createFilm(film2);

        mockMvc.perform(get(FILMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(film, film2))));
    }

    @Test
    @Order(2)
    void whenPostRequestCreateAndValidFilm_thenCorrectResponse() throws Exception {
        final var validFilm = getValidFilm();
        validFilm.setId(3L);

        mockMvc.perform(post(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(validFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(validFilm)));

    }

    @Test
    void whenPostRequestCreateAndFilmExists_thenCorrectResponse() throws Exception {
        final var film = getValidFilm();
        filmController.createFilm(film);

        mockMvc.perform(post(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Film already exists",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void whenPostRequestCreateAndInValidFilm_incorrectId_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setId(-1L);

        mockMvc.perform(post(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestCreateAndInValidName_null_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setName(null);

        mockMvc.perform(post(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidName_empty_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setName("");

        mockMvc.perform(post(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidDescription_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setDescription("FilmDescriptionFilmDescriptionFilmDescriptionFilmDescription" +
                "FilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescription" +
                "FilmDescriptionFilmDescriptionFilmDescriptionCH=201");

        mockMvc.perform(post(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidReleaseDate_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setReleaseDate(LocalDate.of(1895, 12, 27));

        mockMvc.perform(post(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestCreateAndInValidDuration_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setDuration(0);

        mockMvc.perform(post(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPutRequestUpdateAndValidFilm_thenCorrectResponse() throws Exception {
        final var film = filmController.createFilm(getValidFilm());
        final var updatedFilm = getValidFilm();
        updatedFilm.setId(film.getId());
        updatedFilm.setName("Updated Test Name");
        updatedFilm.setDescription("Updated Description");
        updatedFilm.setDuration(105);

        mockMvc.perform(put(FILMS_PATH)
                        .content(objectMapper.writeValueAsString(updatedFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()))
                .andExpect(jsonPath("$.name").value("Updated Test Name"))
                .andExpect(jsonPath("$.description")
                        .value("Updated Description"))
                .andExpect(jsonPath("$.duration").value(105));
    }

    @Test
    void whenGetRequestGetFilmById_thenCorrectResponse() throws Exception {
        final var film = filmController.createFilm(getValidFilm());

        mockMvc.perform(get(FILMS_PATH + "/" + film.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));
    }

    @Test
    void whenGetRequestGetPopular_withoutReqParameter_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(FILMS_PATH + "/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetPopular_withReqParameter_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(FILMS_PATH + "/popular?count=4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutRequestAddLike_thenCorrectResponse() throws Exception {
        final var film = filmController.createFilm(getValidFilm());
        final var user = userController.createUser(getValidUser());

        mockMvc.perform(put(FILMS_PATH + "/" + film.getId() + "/like/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, film.getLikes().size());
    }

    @Test
    void whenDeleteRequestDeleteLike_thenCorrectResponse() throws Exception {
        final var film = filmController.createFilm(getValidFilm());
        final var user = userController.createUser(getValidUser());
        film.getLikes().add(user.getId());

        mockMvc.perform(delete(FILMS_PATH + "/" + film.getId() + "/like/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, film.getLikes().size());
    }

    @Test
    void whenDeleteRequestDeleteLike_filmNotExists_thenCorrectResponse() throws Exception {
        final var user = userController.createUser(getValidUser());

        mockMvc.perform(delete(FILMS_PATH + "/" + 9999 + "/like/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteRequestDeleteLike_UserNotExists_thenCorrectResponse() throws Exception {
        final var film = filmController.createFilm(getValidFilm());

        mockMvc.perform(delete(FILMS_PATH + "/" + film.getId() + "/like/" + 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
