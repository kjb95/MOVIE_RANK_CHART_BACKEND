package movierankchart.batch;

import movierankchart.common.exception.ErrorCode;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movierank.repository.MovieRankRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Disabled // 모든 데이터를 확인하는 테스트라 시간이 오래걸려서 비활성화
@SpringBootTest
public class MovieRankChartDataTests {
    @Autowired
    private MovieRankRepository movieRankRepository;
    @Autowired
    private MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;

    @Test
    void 영화_순위_데이터가_잘_저장되었는지_확인() {
        // given
        LocalDate startDate = movieOpenApiHistoryRepository.findStartDate()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MOVIE_OPEN_API_HISTORY_EMPTY.getMessage()));
        LocalDate endDate = movieOpenApiHistoryRepository.findEndDate()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MOVIE_OPEN_API_HISTORY_EMPTY.getMessage()));
        List<LocalDate> datesInRange = DateUtils.getLocalDatesInRange(startDate, endDate);

        // then
        datesInRange.forEach(this::checkMovieRankData);
    }

    private void checkMovieRankData(LocalDate date) {
        List<MovieRank> totalDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.TOTAL_DAILY);
        List<MovieRank> koreanDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.KOREAN_DAILY);
        List<MovieRank> foreignDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.FOREIGN_DAILY);
        List<MovieRank> totalWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.TOTAL_WEEKLY);
        List<MovieRank> koreanWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.KOREAN_WEEKLY);
        List<MovieRank> foreignWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.FOREIGN_WEEKLY);

        Assertions.assertThat(totalDailyMovieRanks.size())
                .isEqualTo(10);
        Assertions.assertThat(koreanDailyMovieRanks.size())
                .isEqualTo(10);
        Assertions.assertThat(foreignDailyMovieRanks.size())
                .isEqualTo(10);
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            Assertions.assertThat(totalWeeklyMovieRanks.size())
                    .isEqualTo(10);
            Assertions.assertThat(koreanWeeklyMovieRanks.size())
                    .isEqualTo(10);
            Assertions.assertThat(foreignWeeklyMovieRanks.size())
                    .isEqualTo(10);
        } else {
            Assertions.assertThat(totalWeeklyMovieRanks.size())
                    .isEqualTo(0);
            Assertions.assertThat(koreanWeeklyMovieRanks.size())
                    .isEqualTo(0);
            Assertions.assertThat(foreignWeeklyMovieRanks.size())
                    .isEqualTo(0);
        }
    }
}
