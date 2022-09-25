package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.Validators.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Validators.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class FilmControllerTest {
    @Autowired
    FilmController filmController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String FILMS_PATH = "/films";

    @Test
    public void whenPostRequestToFilms_requestIsEmpty_thenCorrectResponse() throws Exception {
        String emptyFilm = "";

        mockMvc.perform(post(FILMS_PATH)
                        .content(emptyFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void whenGetRequestToFilms_thenCorrectResponse() throws Exception {
        Film film = createTestFilm(20, "Test Film", "Test Description",
                LocalDate.of(2011, 5, 22), 95);
        Film film2 = createTestFilm(23, "Test Film2", "Test Description2",
                LocalDate.of(2014, 3, 12), 105);

        mockMvc.perform(get(FILMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(film, film2))));
    }

    @Test
    public void whenPostRequestToFilmsAndInValidFilm_incorrectId_thenCorrectResponse() throws Exception {
        String inValidFilm = "{\"id\": -1, \"name\": \"Film Name\", \"description\": \"Film description\"" +
                ", \"releaseDate\": \"2015-03-01\", \"duration\": 100}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(inValidFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Incorrect id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void whenPostRequestToFilmsAndValidFilm_thenCorrectResponse() throws Exception {
        String validFilm = "{\"id\": 1, \"name\": \"Film Name\", \"description\": \"Film description\"" +
                ", \"releaseDate\": \"2015-03-01\", \"duration\": 100}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(validFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(validFilm));
    }

    @Test
    public void whenPostRequestToFilmsAndInValidName_null_thenCorrectResponse() throws Exception {
        String inValidFilm = "{\"id\": 2, \"name\": null, \"description\": \"Film description\"" +
                ", \"releaseDate\": \"2015-03-01\", \"duration\": 100}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(inValidFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToFilmsAndInValidName_empty_thenCorrectResponse() throws Exception {
        String inValidFilm = "{\"id\": 3, \"name\": \"\", \"description\": \"Film description\"" +
                ", \"releaseDate\": \"2015-03-01\", \"duration\": 100}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(inValidFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToFilmsAndInValidDescription_thenCorrectResponse() throws Exception {
        String inValidFilm = "{\"id\": 4, \"name\": \"Film Name\"" +
                ", \"description\": \"FilmDescriptionFilmDescriptionFilmDescriptionFilmDescription" +
                "FilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescription" +
                "FilmDescriptionFilmDescriptionFilmDescriptionCH=201\"" +
                ", \"releaseDate\": \"2015-03-01\", \"duration\": 100}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(inValidFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToFilmsAndValidDescription_thenCorrectResponse() throws Exception {
        String validFilm = "{\"id\": 4, \"name\": \"Film Name\"" +
                ", \"description\": \"FilmDescriptionFilmDescriptionFilmDescriptionFilmDescription" +
                "FilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescription" +
                "FilmDescriptionFilmsDescriptionFilmsCHARACTERS=200\"" +
                ", \"releaseDate\": \"2015-03-01\", \"duration\": 100}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(validFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPostRequestToFilmsAndInValidReleaseDate_thenCorrectResponse() throws Exception {
        String inValidFilm = "{\"id\": 5, \"name\": \"Film Name\", \"description\": \"Film description\"" +
                ", \"releaseDate\": \"1895-12-27\", \"duration\": 100}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(inValidFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToFilmsAndValidReleaseDate_thenCorrectResponse() throws Exception {
        String validFilm = "{\"id\": 5, \"name\": \"Film Name\", \"description\": \"Film description\"" +
                ", \"releaseDate\": \"1895-12-28\", \"duration\": 100}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(validFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPostRequestToFilmsAndInValidDuration_thenCorrectResponse() throws Exception {
        String inValidFilm = "{\"id\": 6, \"name\": \"Film Name\", \"description\": \"Film description\"" +
                ", \"releaseDate\": \"2015-03-01\", \"duration\": 0}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(inValidFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostRequestToFilmsAndValidDuration_thenCorrectResponse() throws Exception {
        String validFilm = "{\"id\": 6, \"name\": \"Film Name\", \"description\": \"Film description\"" +
                ", \"releaseDate\": \"2015-03-01\", \"duration\": 1}";

        mockMvc.perform(post(FILMS_PATH)
                        .content(validFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPutRequestToFilmsAndValidFilm_thenCorrectResponse() throws Exception {
        createTestFilm(7, "Test Film", "Test Description",
                LocalDate.of(2011, 5, 22), 95);
        String validUpdatedFilm = "{\"id\": 7, \"name\": \"Updated Test Film\"" +
                ", \"description\": \"Updated Test Description\"" +
                ", \"releaseDate\": \"2012-05-22\", \"duration\": 105}";

        mockMvc.perform(put(FILMS_PATH)
                        .content(validUpdatedFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Updated Test Film"))
                .andExpect(jsonPath("$.description")
                        .value("Updated Test Description"))
                .andExpect(jsonPath("$.releaseDate")
                        .value("2012-05-22"))
                .andExpect(jsonPath("$.duration").value(105));
    }

    @Test
    public void whenPutRequestToFilmsAndInValidFilm_thenCorrectResponse() throws Exception {
        createTestFilm(8, "Test Film", "Test Description",
                LocalDate.of(2011, 5, 22), 95);
        String inValidUpdatedFilm = "{\"id\": -1, \"name\": \"Updated Test Film\"" +
                ", \"description\": \"Updated Test Description\"" +
                ", \"releaseDate\": \"2012-05-22\", \"duration\": 105}";

        mockMvc.perform(put(FILMS_PATH)
                        .content(inValidUpdatedFilm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Incorrect id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private Film createTestFilm(long id, String name, String description, LocalDate releaseDate, int duration)
            throws BadRequestException, NotFoundException {

        Film film = Film.builder().id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .build();

        filmController.createFilm(film);
        return film;
    }
}
