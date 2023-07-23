package movierankchart.domain.movierank.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@AllArgsConstructor
@Getter
public class FindMovieRankLineChartVo {
    private String movieTitle;
    private Map<LocalDate, Long> data;
}
