package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindMovieRankLineChartResponseDto {
    private String movieTitle;
    private long[] datas;
}
