package movierankchart.batch.scheduler;

import lombok.RequiredArgsConstructor;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SaveMovieRankScheduler {
    private final JobLauncher jobLauncher;
    private final Job saveMovieRankJob;
    @Scheduled(fixedDelay = 1000000)
    public void saveMovieData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, JobRestartException {
        Map<String, JobParameter> jobParameterMap = new HashMap<>();
        Date currentDate = new Date(System.currentTimeMillis());
        jobParameterMap.put("currentDate", new JobParameter(currentDate));
        jobParameterMap.put("currentTime", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(jobParameterMap);
        jobLauncher.run(saveMovieRankJob, jobParameters);
    }
}
