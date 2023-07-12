package movierankchart.batch.reader;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.kobis.service.KobisService;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SaveMovieRankReader implements ItemReader<KobisMovieRankResponseDto> {
    private final KobisService kobisService;
    private int openApiCallCount = 0;
    private LocalDate startDate;
    private MovieRankType movieRankType;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        String startDateString = (String) stepExecution.getJobExecution()
                .getExecutionContext()
                .get(BatchConstants.START_DATE);
        startDate = DateUtils.stringToLocalDate(startDateString, BatchConstants.YYYYMMDD);
        String stepName = stepExecution.getStepName();
        movieRankType = MovieRankType.findStepTypeByStepName(stepName);
    }

    @Override
    public KobisMovieRankResponseDto read() {
        LocalDate date = startDate.minusDays(KobisConstants.DAILY_API_CALLS)
                .plusDays(openApiCallCount * movieRankType.getDateInterval());
        if (++openApiCallCount >= movieRankType.getApiCallCount()) {
            openApiCallCount = 0;
            return null;
        }
        return kobisService.findMovieRank(date, movieRankType.getRepNationCd(), movieRankType.getKobisApiPath());
    }
}
