package movierankchart.batch.processor;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kmdb.service.KmdbService;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.movierank.entity.MovieRankBase;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SaveMovieRankProcessor implements ItemProcessor<KobisMovieRankResponseDto, List<MovieRankBase>> {
    private final KmdbService kmdbService;
    private MovieRankType movieRankType;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        movieRankType = MovieRankType.findStepTypeByStepName(stepName);
    }
    @Override
    public List<MovieRankBase> process(KobisMovieRankResponseDto item) {
        String showRange = item.getBoxOfficeResult()
                .getShowRange();
        return item.getBoxOfficeResult()
                .getDailyBoxOfficeList()
                .stream()
                .map(kobisBoxOfficeResponseDto -> createMovieTotalDaily(showRange, kobisBoxOfficeResponseDto))
                .collect(Collectors.toList());
    }

    private MovieRankBase createMovieTotalDaily(String showRange, KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto) {
        KmdbResultResponseDto kmdbResultResponseDto = kmdbService.findMovieDetail(kobisBoxOfficeResponseDto.getMovieNm(), kobisBoxOfficeResponseDto.getMovieCd(), kobisBoxOfficeResponseDto.getOpenDt());
        MovieRankBase movieRankBase = movieRankType.getMovieRankBase();
        return movieRankBase.createMovieRank(showRange, kobisBoxOfficeResponseDto, kmdbResultResponseDto);
    }
}
