package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidFilm;

@WebMvcTest(controllers = FilmController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    private static final String BASE_URI = "/films";

    @Test
    void whenGetRequestGetAllFilms_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetFilmById_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/" + 1))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetFilmById_incorrectId_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/" + null))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenGetRequestGetPopular_withoutReqParameter_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/popular"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetPopular_withReqParameter_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/popular?count=4"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetRequestGetPopular_nullReqParameter_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/popular?count=null"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenGetRequestGetPopular_negativeReqParameter_thenCorrectResponse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/popular?count=-1"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestIsEmpty_thenCorrectResponse() throws Exception {
        mockMvc.perform(post(BASE_URI))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestSave_thenCorrectResponse() throws Exception {
        final var film = getValidFilm();
        when(filmService.save(film)).thenReturn(film);

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestSaveAndInValidFilm_incorrectId_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setId(-1L);

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestSaveAndInValidName_null_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setName(null);

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestSaveAndInValidName_empty_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setName("");

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestSaveAndInValidDescription_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setDescription("FilmDescriptionFilmDescriptionFilmDescriptionFilmDescription" +
                "FilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescriptionFilmDescription" +
                "FilmDescriptionFilmDescriptionFilmDescriptionCH=201");

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestSaveAndInValidReleaseDate_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setReleaseDate(LocalDate.of(1895, 12, 27));

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPostRequestSaveAndInValidDuration_thenCorrectResponse() throws Exception {
        final var inValidFilm = getValidFilm();
        inValidFilm.setDuration(0);

        mockMvc.perform(post(BASE_URI)
                        .content(objectMapper.writeValueAsString(inValidFilm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenPutRequestUpdateAndValidFilm_thenCorrectResponse() throws Exception {
        final var film = getValidFilm();
        when(filmService.update(film)).thenReturn(film);

        mockMvc.perform(put(BASE_URI)
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutRequestAddLike_thenCorrectResponse() throws Exception {
        mockMvc.perform(put(BASE_URI + "/" + 1 + "/like/" + 1))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteRequestDeleteLike_thenCorrectResponse() throws Exception {
        mockMvc.perform(delete(BASE_URI + "/" + 1 + "/like/" + 1))
                .andExpect(status().isOk());
    }
}
