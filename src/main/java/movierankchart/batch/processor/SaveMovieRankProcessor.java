package movierankchart.batch.processor;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchConstants;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kmdb.service.KmdbService;
import movierankchart.domain.kobis.constants.KobisConstants;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.movies.repository.MoviesRepository;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SaveMovieRankProcessor implements ItemProcessor<KobisMovieRankResponseDto, List<MovieRank>> {
    private final KmdbService kmdbService;
    private final MoviesRepository moviesRepository;
    private MovieRankType movieRankType;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        movieRankType = MovieRankType.findStepTypeByStepName(stepName);
    }

    @Override
    public List<MovieRank> process(KobisMovieRankResponseDto item) {
        String showRange = item.getBoxOfficeResult()
                .getShowRange();
        List<KobisBoxOfficeResponseDto> boxOfficeList = selectBoxOfficeListType(item);
        return boxOfficeList.stream()
                .map(kobisBoxOfficeResponseDto -> createMovieRank(showRange, kobisBoxOfficeResponseDto))
                .collect(Collectors.toList());
    }

    private List<KobisBoxOfficeResponseDto> selectBoxOfficeListType(KobisMovieRankResponseDto item) {
        if (movieRankType.getKobisApiPath()
                .equals(KobisConstants.DAILY_BOX_OFFICE_PATH)) {
            return item.getBoxOfficeResult()
                    .getDailyBoxOfficeList();
        }
        return item.getBoxOfficeResult()
                .getWeeklyBoxOfficeList();
    }

    private MovieRank createMovieRank(String showRange, KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto) {
        String openDtString = kobisBoxOfficeResponseDto.getOpenDt()
                .replace(KobisConstants.OPEN_DT_DELIMITER, "");
        LocalDate openDt = DateUtils.stringToLocalDate(openDtString, BatchConstants.YYYYMMDD);
        Movies movies = moviesRepository.findMoviesByTitleAndOpeningDate(kobisBoxOfficeResponseDto.getMovieNm(), openDt)
                .orElseGet(() -> findMovieDetail(kobisBoxOfficeResponseDto));
        return MovieRank.createMovieRank(showRange, kobisBoxOfficeResponseDto, movies, movieRankType);
    }

    private Movies findMovieDetail(KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto) {
        KmdbResultResponseDto kmdbResultResponseDto = kmdbService.findMovieDetail(kobisBoxOfficeResponseDto.getMovieNm(), kobisBoxOfficeResponseDto.getMovieCd(), kobisBoxOfficeResponseDto.getOpenDt());
        return Movies.fromDto(kmdbResultResponseDto);
    }
}
