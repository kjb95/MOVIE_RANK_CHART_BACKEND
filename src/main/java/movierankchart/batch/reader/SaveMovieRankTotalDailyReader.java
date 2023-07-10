package movierankchart.batch.reader;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.kobis.service.KobisService;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SaveMovieRankTotalDailyReader implements ItemReader<KobisMovieRankResponseDto> {
    private ExecutionContext jobExecutionContext;
    private final KobisService kobisService;
    private int openApiCallCount = 0;


    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecutionContext = stepExecution.getJobExecution()
                .getExecutionContext();
    }

    @Override
    public KobisMovieRankResponseDto read() {
        if (openApiCallCount >= KobisConstants.DAILY_API_CALLS) {
            return null;
        }
        String dateString = (String)jobExecutionContext.get(BatchConstants.START_DATE);
        LocalDate startDateStored = DateUtils.stringToLocalDate(dateString, BatchConstants.YYYYMMDD);
        LocalDate date = startDateStored.minusDays(KobisConstants.DAILY_API_CALLS).plusDays(openApiCallCount++);
        return kobisService.findMovieRank(date, null, KobisConstants.DAILY_BOX_OFFICE_PATH);
    }
}