package movierankchart.batch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.batch.constants.BatchErrorMessage;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movieopenapihistory.entity.MovieOpenApiHistory;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import movierankchart.domain.movierank.repository.MovieRankRepository;
import movierankchart.domain.movierank.service.MovieRankService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SaveMovieRankRecentWeeklyJobListener {
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;
    private final MovieRankRepository movieRankRepository;
    private final MovieRankService movieRankService;
    private LocalDate endDateWeekly;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        List<MovieOpenApiHistory> movieOpenApiHistories = movieOpenApiHistoryRepository.findAll();
        if (movieOpenApiHistories == null) {
            throw new IllegalArgumentException(BatchErrorMessage.MOVIE_OPEN_API_HISTORY_EMPTY);
        }
        MovieOpenApiHistory movieOpenApiHistory = movieOpenApiHistories.get(0);
        endDateWeekly = movieOpenApiHistory.getEndDateWeekly().plusDays(7);
        String dateString = DateUtils.localDateToString(endDateWeekly, BatchConstants.YYYYMMDD);
        jobExecution.getExecutionContext().put(BatchConstants.END_DATE_WEEKLY, dateString);
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
            return;
        }
        List<LocalDate> datesInRange = DateUtils.getLocalDatesInRange(endDateWeekly, endDateWeekly);
        boolean hasInvalidMovieRankWeeklyData = movieRankService.hasInvalidMovieRankWeeklyData(datesInRange);
        if (hasInvalidMovieRankWeeklyData) {
            log.error(BatchErrorMessage.INVALID_MOVIE_RANK_DATA);
            jobExecution.setStatus(BatchStatus.FAILED);
            return;
        }
        LocalDate endDateWeekly = movieRankRepository.findEaxDateWeekly();
        List<MovieOpenApiHistory> movieOpenApiHistories = movieOpenApiHistoryRepository.findAll();
        if (movieOpenApiHistories == null) {
            throw new IllegalArgumentException(BatchErrorMessage.MOVIE_OPEN_API_HISTORY_EMPTY);
        }
        MovieOpenApiHistory movieOpenApiHistory = movieOpenApiHistories.get(0);
        movieOpenApiHistory.setEndDateWeekly(endDateWeekly);
        movieOpenApiHistoryRepository.save(movieOpenApiHistory);
    }
}
