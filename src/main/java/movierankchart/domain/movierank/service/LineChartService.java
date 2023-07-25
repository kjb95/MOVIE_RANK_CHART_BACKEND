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
import java.util.ArrayList;
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
        List<FindMovieRankLineChartResponseDto> rank = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, this::createNewLineChartDataListRank);
        List<FindMovieRankLineChartResponseDto> audienceCount = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, this::createNewLineChartDataListAudienceCount);
        List<FindMovieRankLineChartResponseDto> sales = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, this::createNewLineChartDataListSales);
        List<FindMovieRankLineChartResponseDto> screeningsCount = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, this::createNewLineChartDataListScreeningsCount);
        List<FindMovieRankLineChartResponseDto> theatersCount = createFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate, this::createNewLineChartDataListTheatersCount);
        return new FindMovieRankLineChartResponseDtos(dates, rank, audienceCount, sales, screeningsCount, theatersCount);
    }

    private List<LineChartData> createNewLineChartDataListRank(MovieRank movieRank) {
        ArrayList<LineChartData> list = new ArrayList<>();
        LineChartData lineChartData = new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankId()
                .getRank());
        list.add(lineChartData);
        return list;
    }

    private List<LineChartData> createNewLineChartDataListSales(MovieRank movieRank) {
        ArrayList<LineChartData> list = new ArrayList<>();
        LineChartData lineChartData = new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankStatistics()
                .getSales());
        list.add(lineChartData);
        return list;
    }

    private List<LineChartData> createNewLineChartDataListAudienceCount(MovieRank movieRank) {
        ArrayList<LineChartData> list = new ArrayList<>();
        LineChartData lineChartData = new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankStatistics()
                .getAudienceCount());
        list.add(lineChartData);
        return list;
    }

    private List<LineChartData> createNewLineChartDataListScreeningsCount(MovieRank movieRank) {
        ArrayList<LineChartData> list = new ArrayList<>();
        LineChartData lineChartData = new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankStatistics()
                .getScreeningsCount());
        list.add(lineChartData);
        return list;
    }

    private List<LineChartData> createNewLineChartDataListTheatersCount(MovieRank movieRank) {
        ArrayList<LineChartData> list = new ArrayList<>();
        LineChartData lineChartData = new LineChartData(movieRank.getMovieRankId()
                .getDate(), movieRank.getMovieRankStatistics()
                .getTheatersCount());
        list.add(lineChartData);
        return list;
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