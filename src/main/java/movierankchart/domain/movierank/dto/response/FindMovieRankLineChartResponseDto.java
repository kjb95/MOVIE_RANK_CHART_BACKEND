package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FindMovieRankLineChartResponseDto {
    private String movieTitle;
    private LocalDate[] dates;
    private long[] datas;
}
