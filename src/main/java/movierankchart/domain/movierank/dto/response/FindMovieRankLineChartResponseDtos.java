package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FindMovieRankLineChartResponseDtos {
    private List<FindMovieRankLineChartResponseDto> rank;
    private List<FindMovieRankLineChartResponseDto> audienceCount;
    private List<FindMovieRankLineChartResponseDto> sales;
    private List<FindMovieRankLineChartResponseDto> screeningsCount;
    private List<FindMovieRankLineChartResponseDto> theatersCount;
}
