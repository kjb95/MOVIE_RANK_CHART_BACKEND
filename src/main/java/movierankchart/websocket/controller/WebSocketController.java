package movierankchart.websocket.controller;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.users.costants.UsersErrorMessage;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.repository.UsersRepository;
import movierankchart.websocket.constants.SendMessageType;
import movierankchart.websocket.domain.UserSubscription;
import movierankchart.websocket.dto.request.SendMessageRequestDto;
import movierankchart.websocket.dto.response.SendMessageResponseDto;
import movierankchart.websocket.dto.response.SubscribedUsersResponseDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final UsersRepository usersRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserSubscription userSubscription;

    // 유저가 입력한 메시지를 해당 채팅방에 전송
    @MessageMapping("/send-message/{chatRoomId}")
    public void handleSendMessage(@DestinationVariable String chatRoomId, SendMessageRequestDto sendMessageRequestDto, Principal principal) {
        Users users = usersRepository.findUsersByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException(UsersErrorMessage.NOT_FOUND_USER_EMAIL));
        SubscribedUsersResponseDto subscribedUsersResponseDto = userSubscription.findSubscribedUsers(Long.parseLong(chatRoomId));
        SendMessageResponseDto sendMessageResponseDto = SendMessageResponseDto.builder()
                .sendMessageType(SendMessageType.TALK)
                .id(users.getUsersId())
                .time(LocalDateTime.now())
                .name(users.getName())
                .content(sendMessageRequestDto.getContent())
                .picture(users.getPicture())
                .subscribedUsers(subscribedUsersResponseDto)
                .build();
        simpMessagingTemplate.convertAndSend("/topic/" + chatRoomId, sendMessageResponseDto);
    }
}
