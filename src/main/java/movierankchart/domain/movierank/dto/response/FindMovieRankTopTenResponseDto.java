package movierankchart.domain.movierank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FindMovieRankTopTenResponseDto {
    private boolean isNewRank;
    private int rank;
    private int rankIncrement;
    private long audienceCount;
    private String title;
    private String poster;
    private LocalDate openingDate;
}
