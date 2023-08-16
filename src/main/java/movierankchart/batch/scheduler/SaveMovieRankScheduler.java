package movierankchart.batch.scheduler;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchErrorMessage;
import movierankchart.domain.movieopenapihistory.entity.MovieOpenApiHistory;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SaveMovieRankScheduler {
    private final JobLauncher jobLauncher;
    private final Job saveMovieRankPastJob;
    private final Job saveMovieRankRecentDailyJob;
    private final Job saveMovieRankRecentWeeklyJob;
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;

//    @Scheduled(fixedDelay = 900000)
//    public void saveMovieRankPastJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        Map<String, JobParameter> jobParameterMap = new HashMap<>();
//        jobParameterMap.put("currentTime", new JobParameter(System.currentTimeMillis()));
//        JobParameters jobParameters = new JobParameters(jobParameterMap);
//        jobLauncher.run(saveMovieRankPastJob, jobParameters);
//    }

    @Scheduled(fixedDelay = 10800000)
    public void saveMovieRankRecentDailyJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        List<MovieOpenApiHistory> movieOpenApiHistories = movieOpenApiHistoryRepository.findAll();
        if (movieOpenApiHistories == null) {
            throw new IllegalArgumentException(BatchErrorMessage.MOVIE_OPEN_API_HISTORY_EMPTY);
        }
        MovieOpenApiHistory movieOpenApiHistory = movieOpenApiHistories.get(0);
        LocalDate endDateDaily = movieOpenApiHistory.getEndDateDaily();
        LocalDate currentDate = LocalDate.now();
        if (ChronoUnit.DAYS.between(endDateDaily, currentDate) == 1) {
            return;
        }
        Map<String, JobParameter> jobParameterMap = new HashMap<>();
        jobParameterMap.put("currentTime", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(jobParameterMap);
        jobLauncher.run(saveMovieRankRecentDailyJob, jobParameters);
    }

    @Scheduled(fixedDelay = 10800000)
    public void saveMovieRankRecentWeeklyJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        List<MovieOpenApiHistory> movieOpenApiHistories = movieOpenApiHistoryRepository.findAll();
        if (movieOpenApiHistories == null) {
            throw new IllegalArgumentException(BatchErrorMessage.MOVIE_OPEN_API_HISTORY_EMPTY);
        }
        MovieOpenApiHistory movieOpenApiHistory = movieOpenApiHistories.get(0);
        LocalDate endDateWeekly = movieOpenApiHistory.getEndDateWeekly();
        LocalDate currentDate = LocalDate.now();
        if (ChronoUnit.DAYS.between(endDateWeekly, currentDate) < 14) {
            return;
        }
        Map<String, JobParameter> jobParameterMap = new HashMap<>();
        jobParameterMap.put("currentTime", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(jobParameterMap);
        jobLauncher.run(saveMovieRankRecentWeeklyJob, jobParameters);
    }
}
