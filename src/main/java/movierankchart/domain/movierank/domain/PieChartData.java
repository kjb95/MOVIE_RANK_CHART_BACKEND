package movierankchart.domain.movierank.domain;

import movierankchart.domain.movierank.dto.response.FindMovieRankPieChartResponseDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankPieChartResponseDtos;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movierank.entity.MovieRankStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PieChartData {
    private Map<String, Long> sales = new HashMap<>();
    private Map<String, Long> audienceCount = new HashMap<>();
    private Map<String, Long> screeningsCount = new HashMap<>();
    private Map<String, Long> theatersCount = new HashMap<>();

    public PieChartData(List<MovieRank> movieRanks) {
        movieRanks.forEach(this::putPieChartData);
    }

    public void putPieChartData(MovieRank movieRank) {
        MovieRankStatistics movieRankStatistics = movieRank.getMovieRankStatistics();
        String title = movieRank.getMovies()
                .getTitle();
        Long salesSum = sales.computeIfAbsent(title, key -> 0L);
        Long audienceCountSum = audienceCount.computeIfAbsent(title, key -> 0L);
        Long ascreeningsCountSum = screeningsCount.computeIfAbsent(title, key -> 0L);
        Long theatersCountSum = theatersCount.computeIfAbsent(title, key -> 0L);
        sales.put(title, salesSum + movieRankStatistics.getSales());
        audienceCount.put(title, audienceCountSum + movieRankStatistics.getAudienceCount());
        screeningsCount.put(title, ascreeningsCountSum + movieRankStatistics.getScreeningsCount());
        theatersCount.put(title, theatersCountSum + movieRankStatistics.getTheatersCount());
    }

    public FindMovieRankPieChartResponseDtos toFindMovieRankPieChartResponseDtos() {
        return new FindMovieRankPieChartResponseDtos(createFindMovieRankPieChartResponseDtos(sales), createFindMovieRankPieChartResponseDtos(audienceCount), createFindMovieRankPieChartResponseDtos(screeningsCount), createFindMovieRankPieChartResponseDtos(theatersCount));
    }

    private List<FindMovieRankPieChartResponseDto> createFindMovieRankPieChartResponseDtos(Map<String, Long> data) {
        return data.entrySet()
                .stream()
                .map(salesEntry -> new FindMovieRankPieChartResponseDto(salesEntry.getKey(), salesEntry.getValue()))
                .collect(Collectors.toList());
    }
}
