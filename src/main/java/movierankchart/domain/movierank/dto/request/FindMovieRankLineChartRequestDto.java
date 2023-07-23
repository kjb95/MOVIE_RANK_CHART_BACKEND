package movierankchart.domain.movierank.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class FindMovieRankLineChartRequestDto {
    @DateTimeFormat(pattern = "yyyyMMdd")
    @NotNull
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyyMMdd")
    @NotNull
    private LocalDate endDate;
}
