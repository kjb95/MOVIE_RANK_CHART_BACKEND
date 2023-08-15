package movierankchart.domain.movieopenapihistory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FindMovieRankDataRangeResponseDto {
    private LocalDate startDate;
    private LocalDate endDateDaily;
}
