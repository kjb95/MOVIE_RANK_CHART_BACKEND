package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindMovieRankPieChartResponseDto {
    private String movieTitle;
    private Long value;
}
