package movierankchart.batch.reader;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.kobis.service.KobisService;
import movierankchart.domain.movierank.constants.MovieRankType;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SaveMovieRankRecentWeeklyReader implements ItemReader<KobisMovieRankResponseDto> {
    private final KobisService kobisService;
    private boolean isRead = false;
    private LocalDate endDateWeekly;
    private MovieRankType movieRankType;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        String endDateWeeklyString = (String) stepExecution.getJobExecution()
                .getExecutionContext()
                .get(BatchConstants.END_DATE_WEEKLY);
        endDateWeekly = DateUtils.stringToLocalDate(endDateWeeklyString, BatchConstants.YYYYMMDD);
        String stepName = stepExecution.getStepName();
        movieRankType = MovieRankType.findStepTypeByStepName(stepName);
    }

    @Override
    public KobisMovieRankResponseDto read() {
        if (isRead) {
            isRead = false;
            return null;
        }
        isRead = true;
        return kobisService.findMovieRank(endDateWeekly, movieRankType.getRepNationCd(), movieRankType.getKobisApiPath());
    }
}
