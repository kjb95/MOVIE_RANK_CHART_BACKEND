package movierankchart.websocket.dto.response;

import lombok.Builder;
import lombok.Getter;
import movierankchart.websocket.constants.SendMessageType;

import java.time.LocalDateTime;

@Builder
@Getter
public class SendMessageResponseDto {
    private SendMessageType sendMessageType;
    private Long id;
    private LocalDateTime time;
    private String name;
    private String content;
    private String picture;
    private SubscribedUsersResponseDto subscribedUsers;
}
