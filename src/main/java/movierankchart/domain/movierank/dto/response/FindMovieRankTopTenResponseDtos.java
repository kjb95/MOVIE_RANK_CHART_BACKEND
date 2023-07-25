package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindMovieRankTopTenResponseDtos {
    List<FindMovieRankTopTenResponseDto> findMovieRankTopTenResponseDtos;
}
