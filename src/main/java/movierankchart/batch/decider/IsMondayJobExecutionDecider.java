package movierankchart.batch.decider;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.utils.DateUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class IsMondayJobExecutionDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String endDateString = (String) stepExecution.getJobExecution()
                .getExecutionContext()
                .get(BatchConstants.END_DATE);
        LocalDate endDate = DateUtils.stringToLocalDate(endDateString, BatchConstants.YYYYMMDD);
        return endDate.getDayOfWeek() == DayOfWeek.MONDAY ? new FlowExecutionStatus(BatchConstants.MONDAY) : new FlowExecutionStatus(BatchConstants.NOT_MONDAY);
    }
}
