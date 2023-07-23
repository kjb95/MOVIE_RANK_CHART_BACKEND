package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FindMovieRankLineChartResponseDto {
    private String movieTitle;
    private long[] datas;
}
