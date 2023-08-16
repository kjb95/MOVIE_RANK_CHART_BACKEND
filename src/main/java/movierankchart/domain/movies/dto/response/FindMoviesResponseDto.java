package movierankchart.domain.movies.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FindMoviesResponseDto {
    private long moviesId;
    private String title;
    private LocalDate openingDate;
    private String poster;
    private String genre;
    private String nation;
    private String company;
    private String runtime;
    private String ratingGrade;
    private int chatRoomCount;
}
