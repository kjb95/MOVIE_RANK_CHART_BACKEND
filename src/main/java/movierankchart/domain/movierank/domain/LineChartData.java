package movierankchart.domain.movierank.domain;

import lombok.Getter;
import movierankchart.domain.movierank.dto.response.FindMovieRankLineChartResponseDto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class LineChartData {
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<String, Map<LocalDate, Long>> movieTitleToDateToData = new HashMap<>();

    public LineChartData(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<FindMovieRankLineChartResponseDto> toFindMovieRankLineChartResponseDtos() {
        return movieTitleToDateToData.entrySet()
                .stream()
                .map(this::createFindMovieRankLineChartResponseDto)
                .collect(Collectors.toList());
    }

    private FindMovieRankLineChartResponseDto createFindMovieRankLineChartResponseDto(Map.Entry<String, Map<LocalDate, Long>> movieTitleToDateToDataEntry) {
        String movieTitle = movieTitleToDateToDataEntry.getKey();
        long[] datas = new long[(int) (ChronoUnit.DAYS.between(startDate, endDate) + 1)];
        movieTitleToDateToDataEntry.getValue()
                .entrySet()
                .forEach(dateToDataEntry -> fillDataArray(datas, dateToDataEntry));
        return new FindMovieRankLineChartResponseDto(movieTitle, datas);
    }

    private void fillDataArray(long[] datas, Map.Entry<LocalDate, Long> dateToDataEntry) {
        LocalDate date = dateToDataEntry.getKey();
        int index = (int) ChronoUnit.DAYS.between(startDate, date);
        datas[index] = dateToDataEntry.getValue();
    }
}