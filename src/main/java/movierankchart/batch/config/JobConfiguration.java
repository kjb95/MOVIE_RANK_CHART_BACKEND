package movierankchart.batch.config;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.batch.listener.SaveMovieRankJobListener;
import movierankchart.batch.processor.SaveMovieRankProcessor;
import movierankchart.batch.reader.SaveMovieRankReader;
import movierankchart.batch.writer.SaveMovieRankWriter;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.movierank.entity.MovieRankBase;
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
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SaveMovieRankJobListener saveMovieRankJobListener;
    private final SaveMovieRankReader saveMovieRankReader;
    private final SaveMovieRankProcessor saveMovieRankProcessor;
    private final SaveMovieRankWriter saveMovieRankWriter;

    @Bean
    public Job saveMovieRankJob() {
        return jobBuilderFactory.get("saveMovieRankJob")
                .start(saveMovieRankTotalDailyStep())
                .next(saveMovieRankKoreanDailyStep())
                .listener(saveMovieRankJobListener)
                .build();
    }

    @Bean
    Step saveMovieRankTotalDailyStep() {
        return createMovieRankStep(MovieRankType.TOTAL_DAILY.getStepName());
    }

    @Bean
    Step saveMovieRankKoreanDailyStep() {
        return createMovieRankStep(MovieRankType.KOREAN_DAILY.getStepName());
    }

    private TaskletStep createMovieRankStep(String stepName) {
        return stepBuilderFactory.get(stepName)
                .<KobisMovieRankResponseDto, List<MovieRankBase>>chunk(32)
                .reader(saveMovieRankReader)
                .processor(saveMovieRankProcessor)
                .writer(saveMovieRankWriter)
                .build();
    }
}
