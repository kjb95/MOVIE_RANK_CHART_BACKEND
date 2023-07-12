package movierankchart.domain.movierank.entity;

import lombok.Getter;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.utils.DateUtils;
import movierankchart.common.utils.StringUtils;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
public class MovieRankTotalDaily extends MovieRankBase {

    @Override
    public MovieRankBase createMovieRank(String showRange, KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto, KmdbResultResponseDto kmdbResultResponseDto) {
        MovieRankTotalDaily movieRankTotalDaily = new MovieRankTotalDaily();
        String dateString = StringUtils.subStringUntil(showRange, KobisConstants.SHOW_RANGE_DELIMITER);
        LocalDate date = DateUtils.stringToLocalDate(dateString, BatchConstants.YYYYMMDD);
        movieRankTotalDaily.movieRankId = new MovieRankId(date, Integer.parseInt(kobisBoxOfficeResponseDto.getRank()), MovieRankType.TOTAL_DAILY);
        movieRankTotalDaily.movies = Movies.fromDto(kmdbResultResponseDto);
        movieRankTotalDaily.movieRankStatistics = MovieRankStatistics.fromDto(kobisBoxOfficeResponseDto);
        return movieRankTotalDaily;
    }
}
