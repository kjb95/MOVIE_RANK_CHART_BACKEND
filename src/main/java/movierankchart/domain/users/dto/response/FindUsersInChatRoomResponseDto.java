package movierankchart.domain.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindUsersInChatRoomResponseDto {
    private long usersId;
    private String nickname;
}
