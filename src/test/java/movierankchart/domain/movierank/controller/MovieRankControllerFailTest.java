package movierankchart.domain.movierank.controller;

import movierankchart.batch.scheduler.SaveMovieRankScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MovieRankControllerFailTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SaveMovieRankScheduler saveMovieRankScheduler;

    @Test
    void TOP_10_영화순위_조회시_유효하지않은_날짜_폼_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank").param("date", "202307102")
                .param("movieRankType", "TOTAL_DAILY"));
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void TOP_10_영화순위_조회시_해당날짜에_데이터가_존재하지_않는_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank").param("date", "20230711")
                .param("movieRankType", "TOTAL_WEEKLY"));
        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void TOP_10_영화순위_조회시_유효하지않은_영화순위종류_타입_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank").param("date", "20230710")
                .param("movieRankType", "TOTAL_DAILY2"));
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void TOP_10_영화순위_조회시_날짜_파라미터가_없는_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank").param("movieRankType", "TOTAL_DAILY"));
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void TOP_10_영화순위_조회시_영화순위종류_파라미터가_없는_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank").param("date", "20230710"));
        // then
        resultActions.andExpect(status().isBadRequest());
    }
}