package movierankchart.domain.movierank.entity;

import lombok.*;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.entity.AuditEntity;
import movierankchart.common.utils.DateUtils;
import movierankchart.common.utils.StringUtils;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieRankTotalDaily extends AuditEntity {
    @EmbeddedId
    private MovieRankId movieRankId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Embedded
    private MovieRank movieRank;

    public static MovieRankTotalDaily createMovieRankTotalDaily(String showRange, KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto, KmdbResultResponseDto kmdbResultResponseDto) {
        LocalDate date = computeShowRangeDate(showRange);
        MovieRankId movieRankId = new MovieRankId(date, Integer.parseInt(kobisBoxOfficeResponseDto.getRank()));
        Movies movies = Movies.fromDto(kmdbResultResponseDto);
        MovieRank movieRank = MovieRank.fromDto(kobisBoxOfficeResponseDto);
        return MovieRankTotalDaily.builder()
                .movieRankId(movieRankId)
                .movies(movies)
                .movieRank(movieRank)
                .build();
    }

    private static LocalDate computeShowRangeDate(String showRange) {
        String dateString = StringUtils.subStringUntil(showRange, KobisConstants.SHOW_RANGE_DELIMITER);
        return DateUtils.stringToLocalDate(dateString, BatchConstants.YYYYMMDD);
    }
}
