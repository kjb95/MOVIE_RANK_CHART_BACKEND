package movierankchart.websocket.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequestDto {
    private String content;
}
