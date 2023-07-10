package movierankchart.batch.config;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.listener.SaveMovieRankJobListener;
import movierankchart.batch.processor.SaveMovieRankTotalDailyProcessor;
import movierankchart.batch.reader.SaveMovieRankTotalDailyReader;
import movierankchart.batch.writer.SaveMovieRankTotalDailWriter;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.movierank.entity.MovieRankTotalDaily;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SaveMovieRankJobListener saveMovieRankJobListener;
    private final SaveMovieRankTotalDailyReader saveMovieRankTotalDailyReader;
    private final SaveMovieRankTotalDailyProcessor saveMovieRankTotalDailyProcessor;
    private final SaveMovieRankTotalDailWriter saveMovieRankTotalDailWriter;

    @Bean
    public Job saveMovieRankJob() {
        return jobBuilderFactory.get("saveMovieRankJob")
                .start(saveMovieRankTotalDailyStep())
                .listener(saveMovieRankJobListener)
                .build();
    }

    @Bean
    Step saveMovieRankTotalDailyStep() {
        return stepBuilderFactory.get("saveMovieRankTotalDailyStep")
                .<KobisMovieRankResponseDto, List<MovieRankTotalDaily>>chunk(32)
                .reader(saveMovieRankTotalDailyReader)
                .processor(saveMovieRankTotalDailyProcessor)
                .writer(saveMovieRankTotalDailWriter)
                .build();
    }
}
