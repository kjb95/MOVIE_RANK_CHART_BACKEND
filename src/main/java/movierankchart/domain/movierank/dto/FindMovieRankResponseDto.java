package movierankchart.domain.movierank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FindMovieRankResponseDto {
    private boolean isNewRank;
    private int rank;
    private int rankIncrement;
    private long audienceCount;
    private String title;
    private String poster;
    private LocalDate openingDate;
}
