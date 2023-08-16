package movierankchart.domain.movies.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindMoviesRequestDto {
    // id 또는 title, isConsiderSomeoneChatroom이 입력 되어야 함
    private Long id;
    private String title;
    private Boolean isConsiderSomeoneChatroom; // 채팅방에 사람이 있는 영화만 조회할지에 대한 여부
}
