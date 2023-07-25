package movierankchart.domain.movies.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindMoviesByMovieTitleResponseDtos {
    List<FindMoviesByMovieTitleResponseDto> movies;
}
