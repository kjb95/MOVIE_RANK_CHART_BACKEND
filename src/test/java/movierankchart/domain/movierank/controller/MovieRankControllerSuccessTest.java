package movierankchart.domain.movierank.controller;

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
class MovieRankControllerSuccessTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SaveMovieRankScheduler saveMovieRankScheduler;

    @Test
    void TOP_10_전체일간_영화순위_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/top-ten").param("date", "20230710")
                .param("movieRankType", "TOTAL_DAILY"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void TOP_10_한국일간_영화순위_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/top-ten").param("date", "20230710")
                .param("movieRankType", "KOREAN_DAILY"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void TOP_10_해외일간_영화순위_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/top-ten").param("date", "20230710")
                .param("movieRankType", "FOREIGN_DAILY"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void TOP_10_전체주간_영화순위_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/top-ten").param("date", "20230710")
                .param("movieRankType", "TOTAL_WEEKLY"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void TOP_10_한국주간_영화순위_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/top-ten").param("date", "20230710")
                .param("movieRankType", "KOREAN_WEEKLY"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void TOP_10_해외주간_영화순위_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/top-ten").param("date", "20230710")
                .param("movieRankType", "FOREIGN_WEEKLY"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 라인차트_데이터_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/line-chart").param("startDate", "20230710")
                        .param("endDate", "20230710"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 영화상세_페이지의_라인차트에_필요한_데이터_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/line-chart/58877"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 파이차트에_필요한_데이터_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movie-rank/pie-chart").param("startDate", "20230701")
                .param("endDate", "20230708"));

        // then
        resultActions.andExpect(status().isOk());
    }
}