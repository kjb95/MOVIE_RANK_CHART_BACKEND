package movierankchart.domain.movierank.service;

import movierankchart.domain.movierank.dto.response.FindMovieRankPieChartResponseDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankPieChartResponseDtos;
import movierankchart.domain.movierank.entity.MovieRank;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PieChartService {
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
                        .getTitle(), getValue, Long::sum))
                .entrySet()
                .stream()
                .map(salesEntry -> new FindMovieRankPieChartResponseDto(salesEntry.getKey(), salesEntry.getValue()))
                .collect(Collectors.toList());
    }
}
