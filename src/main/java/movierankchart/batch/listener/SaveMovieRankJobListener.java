package movierankchart.batch.listener;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.exception.ErrorCode;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movieopenapihistory.entity.MovieOpenApiHistory;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import movierankchart.domain.movierank.repository.MovieRankRepository;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class SaveMovieRankJobListener {
    @PersistenceContext
    private EntityManager entityManager;
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;
    private final MovieRankRepository movieRankRepository;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        LocalDate startDate = movieOpenApiHistoryRepository.findById(BatchConstants.MOVIE_OPEN_API_HISTORY_ID)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MOVIE_OPEN_API_HISTORY_ID_NOT_FOUND.getMessage()))
                .getStartDate();
        String dateString = DateUtils.localDateToString(startDate, BatchConstants.YYYYMMDD);
        jobExecution.getExecutionContext()
                .put(BatchConstants.START_DATE, dateString);
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
            return ;
        }
        MovieOpenApiHistory movieOpenApiHistory = movieOpenApiHistoryRepository.findById(BatchConstants.MOVIE_OPEN_API_HISTORY_ID)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.MOVIE_OPEN_API_HISTORY_ID_NOT_FOUND.getMessage()));
        LocalDate minDate = movieRankRepository.findMinDate();
        movieOpenApiHistory.setStartDate(minDate);
        movieOpenApiHistoryRepository.save(movieOpenApiHistory);
    }

}

