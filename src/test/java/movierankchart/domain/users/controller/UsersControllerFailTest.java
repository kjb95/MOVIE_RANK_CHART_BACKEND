package movierankchart.domain.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import movierankchart.batch.scheduler.SaveMovieRankScheduler;
import movierankchart.domain.users.dto.request.UpdateUserChatRoomRequestDto;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
public class UsersControllerFailTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SaveMovieRankScheduler saveMovieRankScheduler;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void 채팅방에_속한_유저_조회시_채팅방_아이디_파라미터가_존재하지_않는_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/users/chatroom"));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 채팅방에_속한_유저_조회시_존재하지_않는_채팅방_아이디_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/users/chatroom").param("moviesId", "999999"));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 채팅방에_속한_유저_조회시_유효하지_않은_채팅방_아이디_값_예외() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/v1/users/chatroom").param("moviesId", "abc"));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 유저의_채팅방_입장시_존재하지않은_유저아이디_값_예외() throws Exception {
        // given
        UpdateUserChatRoomRequestDto updateUserChatRoomRequestDto = new UpdateUserChatRoomRequestDto(58480L);

        // when
        ResultActions resultActions = mvc.perform(patch("/v1/users/999999").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserChatRoomRequestDto)));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 유저의_채팅방_입장시_존재하지않는_채팅방아이디_값_예외() throws Exception {
        // given
        Users users = Users.createUsers("jinbkim@gmail.com", "kim", "https://lh3.googleusercontent.com/a/AAcHTteXwTOgeOZ7HmG4uvVjfngCXEDOcxPt6JiYq8PGFqVI=s96-c");
        usersRepository.save(users);
        users = usersRepository.findUsersByEmail("jinbkim@gmail.com")
                .get();
        UpdateUserChatRoomRequestDto updateUserChatRoomRequestDto = new UpdateUserChatRoomRequestDto(999999L);

        // when
        ResultActions resultActions = mvc.perform(patch("/v1/users/" + users.getUsersId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserChatRoomRequestDto)));

        // then
        resultActions.andExpect(status().isNotFound());
    }
}
