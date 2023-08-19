package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindMovieRankPieChartResponseDto {
    private Long id;
    private String name;
    private Long value;
}
