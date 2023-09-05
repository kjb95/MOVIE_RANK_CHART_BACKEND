package movierankchart.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SubscribedUsersResponseDto {
    List<SubscribedUserResponseDto> subscribedUsers;
}
