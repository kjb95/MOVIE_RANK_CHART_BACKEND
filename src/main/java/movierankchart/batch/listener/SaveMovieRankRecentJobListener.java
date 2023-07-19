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
public class SaveMovieRankRecentJobListener {
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;
    private final MovieRankRepository movieRankRepository;
    private final MovieRankService movieRankService;
    private LocalDate endDate;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        endDate = movieOpenApiHistoryRepository.findEndDate()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MOVIE_OPEN_API_HISTORY_EMPTY.getMessage()))
                .plusDays(1);
        String dateString = DateUtils.localDateToString(endDate, BatchConstants.YYYYMMDD);
        jobExecution.getExecutionContext()
                .put(BatchConstants.END_DATE, dateString);
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
            return;
        }
        List<LocalDate> datesInRange = DateUtils.getLocalDatesInRange(endDate, endDate);
        boolean hasInvalidMovieRankData = movieRankService.hasInvalidMovieRankData(datesInRange);
        if (hasInvalidMovieRankData) {
            jobExecution.setStatus(BatchStatus.FAILED);
            return;
        }
        LocalDate maxDate = movieRankRepository.findMaxDate();
        movieOpenApiHistoryRepository.updateEndDate(maxDate);
    }
}