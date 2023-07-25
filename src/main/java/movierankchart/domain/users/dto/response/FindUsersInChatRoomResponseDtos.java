package movierankchart.domain.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindUsersInChatRoomResponseDtos {
    private List<FindUsersInChatRoomResponseDto> users;
}
