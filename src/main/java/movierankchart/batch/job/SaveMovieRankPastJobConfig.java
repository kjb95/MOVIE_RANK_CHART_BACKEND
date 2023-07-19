package movierankchart.batch.job;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.batch.listener.SaveMovieRankPastJobListener;
import movierankchart.batch.processor.SaveMovieRankProcessor;
import movierankchart.batch.reader.SaveMovieRankPastReader;
import movierankchart.batch.writer.SaveMovieRankWriter;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.entity.MovieRank;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SaveMovieRankPastJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SaveMovieRankPastReader saveMovieRankPastReader;
    private final SaveMovieRankProcessor saveMovieRankProcessor;
    private final SaveMovieRankWriter saveMovieRankWriter;
    private final SaveMovieRankPastJobListener saveMovieRankPastJobListener;

    @Bean
    public Job saveMovieRankPastJob() {
        return jobBuilderFactory.get("saveMovieRankPastJob")
                .start(saveMovieRankPastTotalDailyStep())
                .next(saveMovieRankPastKoreanDailyStep())
                .next(saveMovieRankPastForeignDailyStep())
                .next(saveMovieRankPastTotalWeeklyStep())
                .next(saveMovieRankPastKoreanWeeklyStep())
                .next(saveMovieRankPastForeignWeeklyStep())
                .listener(saveMovieRankPastJobListener)
                .build();
    }

    @Bean
    public Step saveMovieRankPastTotalDailyStep() {
        return createMovieRankPastStep(MovieRankType.TOTAL_DAILY.getStepName());
    }

    @Bean
    public Step saveMovieRankPastKoreanDailyStep() {
        return createMovieRankPastStep(MovieRankType.KOREAN_DAILY.getStepName());
    }

    @Bean
    public Step saveMovieRankPastForeignDailyStep() {
        return createMovieRankPastStep(MovieRankType.FOREIGN_DAILY.getStepName());
    }

    @Bean
    public Step saveMovieRankPastTotalWeeklyStep() {
        return createMovieRankPastStep(MovieRankType.TOTAL_WEEKLY.getStepName());
    }

    @Bean
    public Step saveMovieRankPastKoreanWeeklyStep() {
        return createMovieRankPastStep(MovieRankType.KOREAN_WEEKLY.getStepName());
    }

    @Bean
    public Step saveMovieRankPastForeignWeeklyStep() {
        return createMovieRankPastStep(MovieRankType.FOREIGN_WEEKLY.getStepName());
    }

    private TaskletStep createMovieRankPastStep(String stepName) {
        return stepBuilderFactory.get(stepName)
                .<KobisMovieRankResponseDto, List<MovieRank>>chunk(BatchConstants.DAILY_API_CALLS)
                .reader(saveMovieRankPastReader)
                .processor(saveMovieRankProcessor)
                .writer(saveMovieRankWriter)
                .build();
    }
}
