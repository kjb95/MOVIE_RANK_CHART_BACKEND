package movierankchart.batch.job;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.listener.SaveMovieRankRecentWeeklyJobListener;
import movierankchart.batch.processor.SaveMovieRankProcessor;
import movierankchart.batch.reader.SaveMovieRankRecentWeeklyReader;
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
public class SaveMovieRankRecentWeeklyJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SaveMovieRankRecentWeeklyReader saveMovieRankRecentWeeklyReader;
    private final SaveMovieRankProcessor saveMovieRankProcessor;
    private final SaveMovieRankWriter saveMovieRankWriter;
    private final SaveMovieRankRecentWeeklyJobListener saveMovieRankRecentWeeklyJobListener;

    @Bean
    public Job saveMovieRankRecentWeeklyJob() {
        return jobBuilderFactory.get("saveMovieRankRecentDailyJob")
                .start(saveMovieRankRecentTotalWeeklyStep())
                .next(saveMovieRankRecentKoreanWeeklyStep())
                .next(saveMovieRankRecentForeignWeeklyStep())
                .listener(saveMovieRankRecentWeeklyJobListener)
                .build();
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
                .reader(saveMovieRankRecentWeeklyReader)
                .processor(saveMovieRankProcessor)
                .writer(saveMovieRankWriter)
                .build();
    }
}
