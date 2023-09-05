package movierankchart.websocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubscribedUserResponseDto {
    private Long usersId;
    private String picture;
    private String name;
}
