package movierankchart.domain.movierank.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movierank.dto.response.FindMovieRankPieChartResponseDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankPieChartResponseDtos;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movies.constants.MoviesErrorMessage;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.movies.repository.MoviesRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PieChartService {
    private final MoviesRepository moviesRepository;

    public FindMovieRankPieChartResponseDtos toFindMovieRankPieChartResponseDtos(List<MovieRank> movieRanks) {
        List<FindMovieRankPieChartResponseDto> sales = createFindMovieRankPieChartResponseDtos(movieRanks, movieRank -> movieRank.getMovieRankStatistics()
                .getSales());
        List<FindMovieRankPieChartResponseDto> audienceCount = createFindMovieRankPieChartResponseDtos(movieRanks, movieRank -> movieRank.getMovieRankStatistics()
                .getAudienceCount());
        List<FindMovieRankPieChartResponseDto> screeningsCount = createFindMovieRankPieChartResponseDtos(movieRanks, movieRank -> movieRank.getMovieRankStatistics()
                .getScreeningsCount());
        List<FindMovieRankPieChartResponseDto> theatersCount = createFindMovieRankPieChartResponseDtos(movieRanks, movieRank -> movieRank.getMovieRankStatistics()
                .getTheatersCount());
        return new FindMovieRankPieChartResponseDtos(sales, audienceCount, screeningsCount, theatersCount);
    }

    private List<FindMovieRankPieChartResponseDto> createFindMovieRankPieChartResponseDtos(List<MovieRank> movieRanks, Function<MovieRank, Long> getValue) {
        return movieRanks.stream()
                .collect(Collectors.toMap(movieRank -> movieRank.getMovies()
                        .getMoviesId(), getValue, Long::sum))
                .entrySet()
                .stream()
                .map(this::createFindMovieRankPieChartResponseDto)
                .sorted(Comparator.comparingLong(FindMovieRankPieChartResponseDto::getValue).reversed())
                .collect(Collectors.toList());
    }

    private FindMovieRankPieChartResponseDto createFindMovieRankPieChartResponseDto(Map.Entry<Long, Long> entry) {
        Movies movies = moviesRepository.findById(entry.getKey())
                .orElseThrow(() -> new NoSuchElementException(MoviesErrorMessage.NOT_FOUND_MOVIES));
        return new FindMovieRankPieChartResponseDto(movies.getMoviesId(), movies.getTitle(), entry.getValue());
    }
}
