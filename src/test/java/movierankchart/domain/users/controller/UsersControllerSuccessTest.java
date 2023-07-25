package movierankchart.domain.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import movierankchart.batch.scheduler.SaveMovieRankScheduler;
import movierankchart.domain.users.dto.response.CreateUserRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UsersControllerSuccessTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SaveMovieRankScheduler saveMovieRankScheduler;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 채팅방에_속한_유저_조회_성공() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/users").param("moviesId", "58480"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 회원가입_성공() throws Exception {
        // given
        CreateUserRequestDto requestBody = new CreateUserRequestDto("jinbkim");

        // when
        ResultActions resultActions = mvc.perform(post("/v1/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestBody)));

        // then
        resultActions.andExpect(status().isOk());
    }
}