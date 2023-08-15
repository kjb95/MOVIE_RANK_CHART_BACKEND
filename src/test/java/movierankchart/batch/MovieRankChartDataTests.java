package movierankchart.batch;

import movierankchart.batch.constants.BatchErrorMessage;
import movierankchart.batch.scheduler.SaveMovieRankScheduler;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movieopenapihistory.entity.MovieOpenApiHistory;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import movierankchart.domain.movierank.service.MovieRankService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

@Disabled // 모든 데이터를 확인하는 테스트라 시간이 오래걸려서 비활성화
@SpringBootTest
public class MovieRankChartDataTests {
    @Autowired
    private MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;
    @Autowired
    private MovieRankService movieRankService;
    @MockBean
    private SaveMovieRankScheduler saveMovieRankScheduler;

    @Test
    void 영화_순위_데이터가_잘_저장되었는지_확인() {
        // given
        List<MovieOpenApiHistory> movieOpenApiHistories = movieOpenApiHistoryRepository.findAll();
        if (movieOpenApiHistories == null) {
            throw new IllegalArgumentException(BatchErrorMessage.MOVIE_OPEN_API_HISTORY_EMPTY);
        }
        MovieOpenApiHistory movieOpenApiHistory = movieOpenApiHistories.get(0);
        LocalDate startDate = movieOpenApiHistory.getStartDate();
        LocalDate endDateDaily = movieOpenApiHistory.getEndDateDaily();
        LocalDate endDateWeekly = movieOpenApiHistory.getEndDateWeekly();
        List<LocalDate> dailyDatesInRange = DateUtils.getLocalDatesInRange(startDate, endDateDaily);
        List<LocalDate> weeklyDatesInRange = DateUtils.getLocalDatesInRange(startDate, endDateWeekly);

        // when
        boolean hasInvalidMovieRankDailyData = movieRankService.hasInvalidMovieRankDailyData(dailyDatesInRange);
        boolean hasInvalidMovieRankWeeklyData = movieRankService.hasInvalidMovieRankWeeklyData(weeklyDatesInRange);

        // then
        Assertions.assertThat(hasInvalidMovieRankDailyData).isEqualTo(false);
        Assertions.assertThat(hasInvalidMovieRankWeeklyData).isEqualTo(false);
    }
}
