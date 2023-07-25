package movierankchart.domain.movierank.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindMovieRankPieChartResponseDtos {
    private List<FindMovieRankPieChartResponseDto> sales;
    private List<FindMovieRankPieChartResponseDto> audienceCount;
    private List<FindMovieRankPieChartResponseDto> screeningsCount;
    private List<FindMovieRankPieChartResponseDto> theatersCount;
}
