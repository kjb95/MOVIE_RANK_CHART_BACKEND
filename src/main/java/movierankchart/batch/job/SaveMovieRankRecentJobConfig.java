package movierankchart.batch.job;


import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.batch.decider.IsMondayJobExecutionDecider;
import movierankchart.batch.listener.SaveMovieRankRecentJobListener;
import movierankchart.batch.processor.SaveMovieRankProcessor;
import movierankchart.batch.reader.SaveMovieRankRecentReader;
import movierankchart.batch.writer.SaveMovieRankWriter;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.entity.MovieRank;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SaveMovieRankRecentJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SaveMovieRankRecentReader saveMovieRankRecentReader;
    private final SaveMovieRankProcessor saveMovieRankProcessor;
    private final SaveMovieRankWriter saveMovieRankWriter;
    private final SaveMovieRankRecentJobListener saveMovieRankRecentJobListener;
    private final IsMondayJobExecutionDecider isMondayJobExecutionDecider;

    @Bean
    public Job saveMovieRankRecentJob() {
        return jobBuilderFactory.get("saveMovieRankRecentJob")
                .start(saveMovieRankRecentTotalDailyStep())
                .next(saveMovieRankRecentKoreanDailyStep())
                .next(saveMovieRankRecentForeignDailyStep())
                .next(isMondayJobExecutionDecider)
                .from(isMondayJobExecutionDecider)
                .on(BatchConstants.MONDAY)
                .to(saveMovieRankRecentWeeklyFlow())
                .from(isMondayJobExecutionDecider)
                .on(BatchConstants.NOT_MONDAY)
                .end()
                .end()
                .listener(saveMovieRankRecentJobListener)
                .build();
    }

    @Bean
    public Step saveMovieRankRecentTotalDailyStep() {
        return createMovieRankRecentStep(MovieRankType.TOTAL_DAILY.getStepName());
    }

    @Bean
    public Step saveMovieRankRecentKoreanDailyStep() {
        return createMovieRankRecentStep(MovieRankType.KOREAN_DAILY.getStepName());
    }

    @Bean
    public Step saveMovieRankRecentForeignDailyStep() {
        return createMovieRankRecentStep(MovieRankType.FOREIGN_DAILY.getStepName());
    }

    @Bean
    public Flow saveMovieRankRecentWeeklyFlow() {
        return new FlowBuilder<Flow>("saveMovieRankRecentWeeklyFlow").start(saveMovieRankRecentTotalWeeklyStep())
                .next(saveMovieRankRecentKoreanWeeklyStep())
                .next(saveMovieRankRecentForeignWeeklyStep())
                .end();
    }

    @Bean
    public Step saveMovieRankRecentTotalWeeklyStep() {
        return createMovieRankRecentStep(MovieRankType.TOTAL_WEEKLY.getStepName());
    }

    @Bean
    public Step saveMovieRankRecentKoreanWeeklyStep() {
        return createMovieRankRecentStep(MovieRankType.KOREAN_WEEKLY.getStepName());
    }

    @Bean
    public Step saveMovieRankRecentForeignWeeklyStep() {
        return createMovieRankRecentStep(MovieRankType.FOREIGN_WEEKLY.getStepName());
    }

    private TaskletStep createMovieRankRecentStep(String stepName) {
        return stepBuilderFactory.get(stepName)
                .<KobisMovieRankResponseDto, List<MovieRank>>chunk(1)
                .reader(saveMovieRankRecentReader)
                .processor(saveMovieRankProcessor)
                .writer(saveMovieRankWriter)
                .build();
    }
}
