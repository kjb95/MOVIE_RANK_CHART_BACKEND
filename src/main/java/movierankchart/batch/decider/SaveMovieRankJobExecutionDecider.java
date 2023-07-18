package movierankchart.batch.decider;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movierank.repository.MovieRankRepository;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SaveMovieRankJobExecutionDecider implements JobExecutionDecider {
    private final MovieRankRepository movieRankRepository;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String startDateString = (String) stepExecution.getJobExecution()
                .getExecutionContext()
                .get(BatchConstants.START_DATE);
        LocalDate startDate = DateUtils.stringToLocalDate(startDateString, BatchConstants.YYYYMMDD);
        List<LocalDate> datesInRange = DateUtils.getLocalDatesInRange(startDate, startDate.plusDays(7));
        long invalidDataCount = datesInRange.stream()
                .filter(this::isInvalidMovieRankData)
                .count();
        return invalidDataCount > 0 ? FlowExecutionStatus.FAILED : FlowExecutionStatus.COMPLETED;
    }

    private boolean isInvalidMovieRankData(LocalDate date) {
        List<MovieRank> totalDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.TOTAL_DAILY);
        List<MovieRank> koreanDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.KOREAN_DAILY);
        List<MovieRank> foreignDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.FOREIGN_DAILY);
        List<MovieRank> totalWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.TOTAL_WEEKLY);
        List<MovieRank> koreanWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.KOREAN_WEEKLY);
        List<MovieRank> foreignWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.FOREIGN_WEEKLY);

        if (totalDailyMovieRanks.size() != 10 || koreanDailyMovieRanks.size() != 10 || foreignDailyMovieRanks.size() != 10) {
            return true;
        }
        if ((date.getDayOfWeek() != DayOfWeek.MONDAY) && (totalWeeklyMovieRanks.size() != 0 || koreanWeeklyMovieRanks.size() != 0 || foreignWeeklyMovieRanks.size() != 0)) {
            return true;
        }
        if ((date.getDayOfWeek() == DayOfWeek.MONDAY) && (totalWeeklyMovieRanks.size() != 10 || koreanWeeklyMovieRanks.size() != 10 || foreignWeeklyMovieRanks.size() != 10)) {
            return true;
        }
        return false;
    }
}
