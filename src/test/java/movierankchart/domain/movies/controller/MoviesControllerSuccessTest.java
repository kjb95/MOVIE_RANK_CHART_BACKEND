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
class MoviesControllerSuccessTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SaveMovieRankScheduler saveMovieRankScheduler;

    @Test
    void 영화명으로_영화_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/movies").param("title", "")
                .param("isConsiderSomeoneChatroom", "true"));

        // then
        resultActions.andExpect(status().isOk());
    }
}