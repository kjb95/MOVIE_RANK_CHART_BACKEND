package movierankchart.domain.movierank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FindMovieRankResponseDtos {
    List<FindMovieRankResponseDto> findMovieRankResponseDtos;
}
