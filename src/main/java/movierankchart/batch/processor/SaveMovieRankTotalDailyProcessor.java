package movierankchart.batch.processor;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kmdb.service.KmdbService;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;
import movierankchart.domain.kobis.dto.KobisMovieRankResponseDto;
import movierankchart.domain.movierank.entity.MovieRankTotalDaily;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class SaveMovieRankTotalDailyProcessor implements ItemProcessor<KobisMovieRankResponseDto, List<MovieRankTotalDaily>> {
    private final KmdbService kmdbService;

    @Override
    public List<MovieRankTotalDaily> process(KobisMovieRankResponseDto item) {
        String showRange = item.getBoxOfficeResult()
                .getShowRange();
        return item.getBoxOfficeResult()
                .getDailyBoxOfficeList()
                .stream()
                .map(kobisBoxOfficeResponseDto -> createMovieTotalDaily(showRange, kobisBoxOfficeResponseDto))
                .collect(Collectors.toList());
    }

    private MovieRankTotalDaily createMovieTotalDaily(String showRange, KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto) {
        KmdbResultResponseDto kmdbResultResponseDto = kmdbService.findMovieDetail(kobisBoxOfficeResponseDto.getMovieNm(), kobisBoxOfficeResponseDto.getMovieCd(), kobisBoxOfficeResponseDto.getOpenDt());
        return MovieRankTotalDaily.createMovieRankTotalDaily(showRange, kobisBoxOfficeResponseDto, kmdbResultResponseDto);
    }
}
