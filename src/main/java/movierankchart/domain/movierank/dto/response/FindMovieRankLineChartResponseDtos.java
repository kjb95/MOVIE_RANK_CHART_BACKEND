package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class FindMovieRankLineChartResponseDtos {
    private LocalDate[] dates;
    private List<FindMovieRankLineChartResponseDto> rank;
    private List<FindMovieRankLineChartResponseDto> audienceCount;
    private List<FindMovieRankLineChartResponseDto> sales;
    private List<FindMovieRankLineChartResponseDto> screeningsCount;
    private List<FindMovieRankLineChartResponseDto> theatersCount;
}
