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
public class SaveMovieRankPastJobListener {
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;
    private final MovieRankRepository movieRankRepository;
    private final MovieRankService movieRankService;
    private LocalDate startDate;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        startDate = movieOpenApiHistoryRepository.findStartDate()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MOVIE_OPEN_API_HISTORY_EMPTY.getMessage()));
        String dateString = DateUtils.localDateToString(startDate, BatchConstants.YYYYMMDD);
        jobExecution.getExecutionContext()
                .put(BatchConstants.START_DATE, dateString);
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
            return;
        }
        List<LocalDate> datesInRange = DateUtils.getLocalDatesInRange(startDate, startDate.plusDays(BatchConstants.DAILY_API_CALLS - 1));
        boolean hasInvalidMovieRankData = movieRankService.hasInvalidMovieRankData(datesInRange);
        if (hasInvalidMovieRankData) {
            jobExecution.setStatus(BatchStatus.FAILED);
            return;
        }
        LocalDate minDate = movieRankRepository.findMinDate();
        movieOpenApiHistoryRepository.updateStartDate(minDate);
    }

}

