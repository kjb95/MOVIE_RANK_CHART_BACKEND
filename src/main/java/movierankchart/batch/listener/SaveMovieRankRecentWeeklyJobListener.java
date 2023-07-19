package movierankchart.batch.listener;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.exception.ErrorCode;
import movierankchart.common.utils.DateUtils;
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

@RequiredArgsConstructor
@Component
public class SaveMovieRankRecentWeeklyJobListener {
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;
    private final MovieRankRepository movieRankRepository;
    private final MovieRankService movieRankService;
    private LocalDate endDateWeekly;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        endDateWeekly = movieOpenApiHistoryRepository.findEndDateWeekly()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MOVIE_OPEN_API_HISTORY_EMPTY.getMessage()))
                .plusDays(7);
        String dateString = DateUtils.localDateToString(endDateWeekly, BatchConstants.YYYYMMDD);
        jobExecution.getExecutionContext()
                .put(BatchConstants.END_DATE_WEEKLY, dateString);
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
            return;
        }
        List<LocalDate> datesInRange = DateUtils.getLocalDatesInRange(endDateWeekly, endDateWeekly);
        boolean hasInvalidMovieRankWeeklyData = movieRankService.hasInvalidMovieRankWeeklyData(datesInRange);
        if (hasInvalidMovieRankWeeklyData) {
            jobExecution.setStatus(BatchStatus.FAILED);
            return;
        }
        LocalDate endDateWeekly = movieRankRepository.findEaxDateWeekly();
        movieOpenApiHistoryRepository.updateEndDateWeekly(endDateWeekly);
    }
}
