package movierankchart.batch.job;


import lombok.RequiredArgsConstructor;
import movierankchart.batch.listener.SaveMovieRankRecentDailyJobListener;
import movierankchart.batch.processor.SaveMovieRankProcessor;
import movierankchart.batch.reader.SaveMovieRankRecentDailyReader;
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
public class SaveMovieRankRecentDailyJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SaveMovieRankRecentDailyReader saveMovieRankRecentDailyReader;
    private final SaveMovieRankProcessor saveMovieRankProcessor;
    private final SaveMovieRankWriter saveMovieRankWriter;
    private final SaveMovieRankRecentDailyJobListener saveMovieRankRecentDailyJobListener;

    @Bean
    public Job saveMovieRankRecentDailyJob() {
        return jobBuilderFactory.get("saveMovieRankRecentDailyJob")
                .start(saveMovieRankRecentTotalDailyStep())
                .next(saveMovieRankRecentKoreanDailyStep())
                .next(saveMovieRankRecentForeignDailyStep())
                .listener(saveMovieRankRecentDailyJobListener)
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

    private TaskletStep createMovieRankRecentStep(String stepName) {
        return stepBuilderFactory.get(stepName)
                .<KobisMovieRankResponseDto, List<MovieRank>>chunk(1)
                .reader(saveMovieRankRecentDailyReader)
                .processor(saveMovieRankProcessor)
                .writer(saveMovieRankWriter)
                .build();
    }
}
