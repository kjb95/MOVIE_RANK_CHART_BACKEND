package movierankchart.domain.movierank.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movierank.dto.response.FindMovieRankLineChartResponseDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankLineChartResponseDtos;
import movierankchart.domain.movierank.entity.MovieRank;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LineChartService {
    @AllArgsConstructor
    @Getter
    public class LineChartData {
        private LocalDate date;
        private long value;
    }

    public FindMovieRankLineChartResponseDtos toFindMovieRankLineChartResponseDtos(List<MovieRank> movieRanks, LocalDate startDate, LocalDate endDate) {
        LocalDate[] dates = DateUtils.getLocalDatesInRange(startDate, endDate)
                .toArray(new LocalDate[(int) (ChronoUnit.DAYS.between(startDate, endDate) + 1)]);
        List<FindMovieRankLineChartResponseDto> rank = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, createNewLineChartDataListRank());
        List<FindMovieRankLineChartResponseDto> audienceCount = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, createNewLineChartDataListAudienceCount());
        List<FindMovieRankLineChartResponseDto> sales = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, createNewLineChartDataListSales());
        List<FindMovieRankLineChartResponseDto> screeningsCount = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, createNewLineChartDataListScreeningsCount());
        List<FindMovieRankLineChartResponseDto> theatersCount = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, createNewLineChartDataListTheatersCount());
        return new FindMovieRankLineChartResponseDtos(dates, rank, audienceCount, sales, screeningsCount, theatersCount);
    }

    private Function<MovieRank, List<LineChartData>> createNewLineChartDataListRank() {
        return movieRank -> Arrays.asList(new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankId()
                .getRank()));
    }

    private Function<MovieRank, List<LineChartData>> createNewLineChartDataListSales() {
        return movieRank -> Arrays.asList(new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankStatistics()
                .getSales()));
    }

    private Function<MovieRank, List<LineChartData>> createNewLineChartDataListAudienceCount() {
        return movieRank -> Arrays.asList(new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankStatistics()
                .getAudienceCount()));
    }

    private Function<MovieRank, List<LineChartData>> createNewLineChartDataListScreeningsCount() {
        return movieRank -> Arrays.asList(new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankStatistics()
                .getScreeningsCount()));
    }

    private Function<MovieRank, List<LineChartData>> createNewLineChartDataListTheatersCount() {
        return movieRank -> Arrays.asList(new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankStatistics()
                .getTheatersCount()));
    }


    private List<FindMovieRankLineChartResponseDto> createFindMovieRankLineChartResponseDtos(List<MovieRank> movieRanks, LocalDate startDate, LocalDate endDate, Function<MovieRank, List<LineChartData>> createNewLineChartDataList) {
        return movieRanks.stream()
                .collect(Collectors.toMap(movieRank -> movieRank.getMovies()
                        .getTitle(), createNewLineChartDataList, this::addAllLineChartData))
                .entrySet()
                .stream()
                .map(movieTitleToLineChartDataEntry -> createFindMovieRankLineChartResponseDto(movieTitleToLineChartDataEntry, startDate, endDate))
                .collect(Collectors.toList());
    }

    private List<LineChartData> addAllLineChartData(List<LineChartData> exist, List<LineChartData> replace) {
        exist.addAll(replace);
        return exist;
    }

    private FindMovieRankLineChartResponseDto createFindMovieRankLineChartResponseDto(Map.Entry<String, List<LineChartData>> movieTitleToLineChartDataEntry, LocalDate startDate, LocalDate endDate) {
        String movieTitle = movieTitleToLineChartDataEntry.getKey();
        long[] datas = new long[(int) (ChronoUnit.DAYS.between(startDate, endDate) + 1)];
        movieTitleToLineChartDataEntry.getValue()
                .forEach(lineChartData -> fillDataArray(datas, lineChartData, startDate));
        return new FindMovieRankLineChartResponseDto(movieTitle, datas);
    }

    private void fillDataArray(long[] datas, LineChartData lineChartData, LocalDate startDate) {
        LocalDate date = lineChartData.getDate();
        int index = (int) ChronoUnit.DAYS.between(startDate, date);
        datas[index] = lineChartData.getValue();
    }
}