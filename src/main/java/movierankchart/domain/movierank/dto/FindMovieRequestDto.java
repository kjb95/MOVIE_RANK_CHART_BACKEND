package movierankchart.domain.movierank.dto;

import lombok.Getter;
import lombok.Setter;
import movierankchart.domain.movierank.constants.MovieRankType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class FindMovieRequestDto {
    @DateTimeFormat(pattern = "yyyyMMdd")
    @NotNull
    private LocalDate date;
    @NotNull
    private MovieRankType movieRankType;
}
