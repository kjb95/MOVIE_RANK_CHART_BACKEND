package movierankchart.domain.movies.controller;

import movierankchart.batch.scheduler.SaveMovieRankScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
class MoviesControllerFailTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SaveMovieRankScheduler saveMovieRankScheduler;

    @Test
    void 영화명으로_영화_조회시_유효하지않은_채팅방에_사람이_있는_영화만_조회할지에_대한_여부_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movies").param("title", "").param("isConsiderSomeoneChatroom", "true2"));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 영화명으로_영화_조회시_영화명이_존재하지_않는_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movies").param("isConsiderSomeoneChatroom", "true"));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 영화명으로_영화_조회시_채팅방에_사람이_있는_영화만_조회할지에_대한_여부가_존재하지_않는_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movies").param("title", ""));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 영화아이디로_영화_조회시_유효하지않은_영화아이디_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movies").param("id", "3a"));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 영화아이디_또는_영화제목_둘다_존재하지_않은_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movies"));

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}