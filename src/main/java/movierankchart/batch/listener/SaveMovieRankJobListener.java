package movierankchart.batch.listener;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.error.ErrorCode;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class SaveMovieRankJobListener {
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        LocalDate startDate = movieOpenApiHistoryRepository.findById(BatchConstants.MOVIE_OPEN_API_HISTORY_ID)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MOVIE_OPEN_API_HISTORY_ID_NOT_FOUND.getMessage()))
                .getStartDate();
        String dateString = DateUtils.localDateToString(startDate, BatchConstants.YYYYMMDD);
        jobExecution.getExecutionContext()
                .put(BatchConstants.START_DATE, dateString);
    }
}
