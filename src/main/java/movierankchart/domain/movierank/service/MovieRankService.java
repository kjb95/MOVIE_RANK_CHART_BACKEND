package movierankchart.domain.movierank.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchErrorMessage;
import movierankchart.common.utils.DateUtils;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import movierankchart.domain.movierank.constants.MovieRankConstants;
import movierankchart.domain.movierank.constants.MovieRankErrorMessage;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.dto.request.FindMovieRankLineChartRequestDto;
import movierankchart.domain.movierank.dto.request.FindMovieRankTopTenRequestDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankLineChartResponseDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankLineChartResponseDtos;
import movierankchart.domain.movierank.dto.response.FindMovieRankTopTenResponseDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankTopTenResponseDtos;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movierank.entity.MovieRankStatistics;
import movierankchart.domain.movierank.repository.MovieRankRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieRankService {
    private final MovieRankRepository movieRankRepository;
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;

    public boolean hasInvalidMovieRankData(List<LocalDate> datesInRange) {
        return hasInvalidMovieRankDailyData(datesInRange) || hasInvalidMovieRankWeeklyData(datesInRange);
    }

    public boolean hasInvalidMovieRankDailyData(List<LocalDate> datesInRange) {
        return datesInRange.stream()
                .filter(this::isInvalidMovieRankDailyData)
                .count() > 0;
    }

    public boolean hasInvalidMovieRankWeeklyData(List<LocalDate> datesInRange) {
        return datesInRange.stream()
                .filter(this::isInvalidMovieRankWeeklyData)
                .count() > 0;
    }

    private boolean isInvalidMovieRankDailyData(LocalDate date) {
        List<MovieRank> totalDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.TOTAL_DAILY);
        List<MovieRank> koreanDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.KOREAN_DAILY);
        List<MovieRank> foreignDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.FOREIGN_DAILY);

        if (totalDailyMovieRanks.size() != 10 || koreanDailyMovieRanks.size() != 10 || foreignDailyMovieRanks.size() != 10) {
            return true;
        }
        return false;
    }

    private boolean isInvalidMovieRankWeeklyData(LocalDate date) {
        List<MovieRank> totalWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.TOTAL_WEEKLY);
        List<MovieRank> koreanWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.KOREAN_WEEKLY);
        List<MovieRank> foreignWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.FOREIGN_WEEKLY);

        if ((date.getDayOfWeek() != DayOfWeek.MONDAY) && (totalWeeklyMovieRanks.size() != 0 || koreanWeeklyMovieRanks.size() != 0 || foreignWeeklyMovieRanks.size() != 0)) {
            return true;
        }
        if ((date.getDayOfWeek() == DayOfWeek.MONDAY) && (totalWeeklyMovieRanks.size() != 10 || koreanWeeklyMovieRanks.size() != 10 || foreignWeeklyMovieRanks.size() != 10)) {
            return true;
        }
        return false;
    }

    public FindMovieRankTopTenResponseDtos findMovieRankTopTen(FindMovieRankTopTenRequestDto findMovieRankTopTenRequestDto) {
        List<MovieRank> movieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(findMovieRankTopTenRequestDto.getDate(), findMovieRankTopTenRequestDto.getMovieRankType());
        if (movieRanks.size() == 0) {
            throw new NoSuchElementException(MovieRankErrorMessage.MOVIE_RANK_NOT_FOUND_DATE);
        }
        List<FindMovieRankTopTenResponseDto> findMovieRankTopTenResponseDtos = movieRanks.stream()
                .map(MovieRank::toFindMovieRankTopTenResponseDto)
                .collect(Collectors.toList());
        return new FindMovieRankTopTenResponseDtos(findMovieRankTopTenResponseDtos);
    }

    public FindMovieRankLineChartResponseDtos findMovieRankLineChart(FindMovieRankLineChartRequestDto findMovieRankLineChartRequestDto) {
        LocalDate startDate = findMovieRankLineChartRequestDto.getStartDate();
        LocalDate endDate = findMovieRankLineChartRequestDto.getEndDate();
        validateDate(startDate, endDate);
        List<MovieRank> movieRanks = movieRankRepository.findMovieRankByDate(startDate, endDate);
        LineChartDatas lineChartDatas = new LineChartDatas(movieRanks, startDate, endDate);
        return lineChartDatas.toFindMovieRankLineChartResponseDtos();
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        LocalDate startDateInDb = movieOpenApiHistoryRepository.findStartDate()
                .orElseThrow(() -> new IllegalArgumentException(BatchErrorMessage.MOVIE_OPEN_API_HISTORY_EMPTY));
        LocalDate endDateInDb = movieOpenApiHistoryRepository.findEndDateDaily()
                .orElseThrow(() -> new IllegalArgumentException(BatchErrorMessage.MOVIE_OPEN_API_HISTORY_EMPTY));
        if (startDate.isBefore(startDateInDb) || endDate.isAfter(endDateInDb)) {
            throw new NoSuchElementException(MovieRankErrorMessage.MOVIE_RANK_NOT_FOUND_DATE);
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(MovieRankErrorMessage.INVALID_DATE_RANGE);
        }
        if (ChronoUnit.DAYS.between(startDate, endDate) >= MovieRankConstants.LINE_CHART_SEARCH_MAX_DAYS_RANGE) {
            throw new IllegalArgumentException(String.format(MovieRankErrorMessage.INVALID_LINE_CHART_SEARCH_DAYS_RANGE, MovieRankConstants.LINE_CHART_SEARCH_MAX_DAYS_RANGE));
        }
    }

    @Getter
    public class LineChartDatas {
        private LineChartData rank;
        private LineChartData audienceCount;
        private LineChartData sales;
        private LineChartData screeningsCount;
        private LineChartData theatersCount;

        public LineChartDatas(List<MovieRank> movieRanks, LocalDate startDate, LocalDate endDate) {
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
            Map<LocalDate, Long> dateToData = lineChartData.movieTitleToDateToData.computeIfAbsent(title, key -> new HashMap<>());
            dateToData.put(date, data);
        }

        public FindMovieRankLineChartResponseDtos toFindMovieRankLineChartResponseDtos() {
            return new FindMovieRankLineChartResponseDtos(rank.toFindMovieRankLineChartResponseDtos(), audienceCount.toFindMovieRankLineChartResponseDtos(), sales.toFindMovieRankLineChartResponseDtos(), screeningsCount.toFindMovieRankLineChartResponseDtos(), theatersCount.toFindMovieRankLineChartResponseDtos());
        }
    }

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
            int totalDaysCount = (int) (ChronoUnit.DAYS.between(startDate, endDate) + 1);
            LocalDate[] dates = DateUtils.getLocalDatesInRange(startDate, endDate)
                    .toArray(new LocalDate[totalDaysCount]);
            long[] datas = new long[totalDaysCount];

            movieTitleToDateToDataEntry.getValue()
                    .entrySet()
                    .forEach(dateToDataEntry -> fillDataArray(datas, dateToDataEntry));
            return new FindMovieRankLineChartResponseDto(movieTitle, dates, datas);
        }

        private void fillDataArray(long[] datas, Map.Entry<LocalDate, Long> dateToDataEntry) {
            LocalDate date = dateToDataEntry.getKey();
            int index = (int) ChronoUnit.DAYS.between(startDate, date);
            datas[index] = dateToDataEntry.getValue();
        }
    }
}