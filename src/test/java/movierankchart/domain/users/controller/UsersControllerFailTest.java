package movierankchart.domain.users.controller;

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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UsersControllerFailTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SaveMovieRankScheduler saveMovieRankScheduler;

    @Test
    void 채팅방에_속한_유저_조회시_채팅방_아이디_파라미터가_존재하지_않는_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/users"));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 채팅방에_속한_유저_조회시_존재하지_않는_채팅방_아이디_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/users").param("moviesId", "999999"));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 채팅방에_속한_유저_조회시_유효하지_않은_채팅방_아이디_값_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/users").param("moviesId", "abc"));

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
