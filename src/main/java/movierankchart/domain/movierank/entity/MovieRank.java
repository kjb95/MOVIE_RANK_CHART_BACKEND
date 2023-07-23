package movierankchart.domain.movierank.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.entity.AuditEntity;
import movierankchart.common.utils.DateUtils;
import movierankchart.common.utils.StringUtils;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.dto.response.FindMovieRankTopTenResponseDto;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieRank extends AuditEntity {
    @EmbeddedId
    protected MovieRankId movieRankId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movies_id")
    protected Movies movies;
    @Embedded
    protected MovieRankStatistics movieRankStatistics;

    public static MovieRank createMovieRank(String showRange, KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto, Movies movies, MovieRankType movieRankType) {
        MovieRank movieRank = new MovieRank();
        String dateString = StringUtils.subStringUntil(showRange, KobisConstants.SHOW_RANGE_DELIMITER);
        LocalDate date = DateUtils.stringToLocalDate(dateString, BatchConstants.YYYYMMDD);
        movieRank.movieRankId = new MovieRankId(date, Integer.parseInt(kobisBoxOfficeResponseDto.getRnum()), movieRankType);
        movieRank.movies = movies;
        movieRank.movieRankStatistics = MovieRankStatistics.fromDto(kobisBoxOfficeResponseDto);
        return movieRank;
    }

    public FindMovieRankTopTenResponseDto toFindMovieRankTopTenResponseDto() {
        return new FindMovieRankTopTenResponseDto(movieRankStatistics.isNewRank(), movieRankId.getRank(), movieRankStatistics.getRankIncrement(), movieRankStatistics.getAudienceCount(), movies.getTitle(), movies.getPoster(), movies.getOpeningDate());
    }
}
