package movierankchart.domain.movies.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FindMoviesByMovieTitleRequestDto {
    @NotNull
    private String title;
    @NotNull
    private Boolean isConsiderSomeoneChatroom; // 채팅방에 사람이 있는 영화만 조회할지에 대한 여부
}
