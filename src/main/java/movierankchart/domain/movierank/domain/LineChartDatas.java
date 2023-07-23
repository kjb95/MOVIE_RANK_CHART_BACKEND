package movierankchart.domain.movierank.domain;

import lombok.Getter;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movierank.dto.response.FindMovieRankLineChartResponseDtos;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movierank.entity.MovieRankStatistics;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class LineChartDatas {
    private LocalDate startDate;
    private LocalDate endDate;
    private LineChartData rank;
    private LineChartData audienceCount;
    private LineChartData sales;
    private LineChartData screeningsCount;
    private LineChartData theatersCount;

    public LineChartDatas(List<MovieRank> movieRanks, LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        rank = new LineChartData(startDate, endDate);
        audienceCount = new LineChartData(startDate, endDate);
        sales = new LineChartData(startDate, endDate);
        screeningsCount = new LineChartData(startDate, endDate);
        theatersCount = new LineChartData(startDate, endDate);
        movieRanks.forEach(this::putLineChartData);
    }

    private void putLineChartData(MovieRank movieRank) {
        if (movieRank.getMovies() == null) {
            return;
        }
        String title = movieRank.getMovies()
                .getTitle();
        LocalDate date = movieRank.getMovieRankId()
                .getDate();
        long rankData = movieRank.getMovieRankId()
                .getRank();
        MovieRankStatistics movieRankStatistics = movieRank.getMovieRankStatistics();
        Long audienceCountData = movieRankStatistics.getAudienceCount();
        Long screeningsCountData = movieRankStatistics.getScreeningsCount();
        Long salesData = movieRankStatistics.getSales();
        Long theatersCountData = movieRankStatistics.getTheatersCount();
        putLineChartData(rank, title, date, rankData);
        putLineChartData(audienceCount, title, date, audienceCountData);
        putLineChartData(sales, title, date, salesData);
        putLineChartData(screeningsCount, title, date, screeningsCountData);
        putLineChartData(theatersCount, title, date, theatersCountData);
    }

    private void putLineChartData(LineChartData lineChartData, String title, LocalDate date, Long data) {
        Map<LocalDate, Long> dateToData = lineChartData.getMovieTitleToDateToData().computeIfAbsent(title, key -> new HashMap<>());
        dateToData.put(date, data);
    }

    public FindMovieRankLineChartResponseDtos toFindMovieRankLineChartResponseDtos() {
        LocalDate[] dates = DateUtils.getLocalDatesInRange(startDate, endDate)
                .toArray(new LocalDate[(int) (ChronoUnit.DAYS.between(startDate, endDate) + 1)]);
        return new FindMovieRankLineChartResponseDtos(dates, rank.toFindMovieRankLineChartResponseDtos(), audienceCount.toFindMovieRankLineChartResponseDtos(), sales.toFindMovieRankLineChartResponseDtos(), screeningsCount.toFindMovieRankLineChartResponseDtos(), theatersCount.toFindMovieRankLineChartResponseDtos());
    }
}