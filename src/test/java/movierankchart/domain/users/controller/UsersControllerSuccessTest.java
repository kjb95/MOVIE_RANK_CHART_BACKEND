package movierankchart.domain.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import movierankchart.batch.scheduler.SaveMovieRankScheduler;
import movierankchart.domain.movies.repository.MoviesRepository;
import movierankchart.domain.users.dto.request.UpdateUserChatRoomRequestDto;
import movierankchart.domain.users.dto.response.CreateUserRequestDto;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private MoviesRepository moviesRepository;

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
        ResultActions resultActions = mvc.perform(post("/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 유저의_채팅방_입장_성공() throws Exception {
        // given
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto("jinbkim");
        mvc.perform(post("/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDto)));
        Users users = usersRepository.findUsersByNickname("jinbkim")
                .get();
        Long usersId = users.getUsersId();
        UpdateUserChatRoomRequestDto requestBody = new UpdateUserChatRoomRequestDto(58480L);

        // when
        ResultActions resultActions = mvc.perform(patch("/v1/users/" + usersId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // then
        resultActions.andExpect(status().isOk());
        Assertions.assertThat(users.getMovies()
                        .getMoviesId())
                .isEqualTo(58480);
    }

    @Test
    void 유저의_채팅방_나가기_성공() throws Exception {
        // given
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto("jinbkim");
        mvc.perform(post("/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDto)));
        Users users = usersRepository.findUsersByNickname("jinbkim")
                .get();
        Long usersId = users.getUsersId();
        UpdateUserChatRoomRequestDto updateUserChatRoomRequestDto = new UpdateUserChatRoomRequestDto(58480L);
        mvc.perform(patch("/v1/users/" + usersId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserChatRoomRequestDto)));
        UpdateUserChatRoomRequestDto requestBody = new UpdateUserChatRoomRequestDto();

        // when
        ResultActions resultActions = mvc.perform(patch("/v1/users/" + usersId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // then
        resultActions.andExpect(status().isOk());
        Assertions.assertThat(users.getMovies())
                .isNull();
    }
}