package movierankchart.domain.movierank.service;

import lombok.RequiredArgsConstructor;
import movierankchart.batch.constants.BatchErrorMessage;
import movierankchart.domain.movieopenapihistory.entity.MovieOpenApiHistory;
import movierankchart.domain.movieopenapihistory.repository.MovieOpenApiHistoryRepository;
import movierankchart.domain.movierank.constants.MovieRankConstants;
import movierankchart.domain.movierank.constants.MovieRankErrorMessage;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.dto.request.FindMovieRankLineChartRequestDto;
import movierankchart.domain.movierank.dto.request.FindMovieRankPieChartRequestDto;
import movierankchart.domain.movierank.dto.request.FindMovieRankTopTenRequestDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankLineChartResponseDtos;
import movierankchart.domain.movierank.dto.response.FindMovieRankPieChartResponseDtos;
import movierankchart.domain.movierank.dto.response.FindMovieRankTopTenResponseDto;
import movierankchart.domain.movierank.dto.response.FindMovieRankTopTenResponseDtos;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movierank.repository.MovieRankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MovieRankService {
    private final MovieRankRepository movieRankRepository;
    private final MovieOpenApiHistoryRepository movieOpenApiHistoryRepository;
    private final PieChartService pieChartService;
    private final LineChartService lineChartService;

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
        return lineChartService.toFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate);
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        List<MovieOpenApiHistory> movieOpenApiHistories = movieOpenApiHistoryRepository.findAll();
        if (movieOpenApiHistories == null) {
            throw new IllegalArgumentException(BatchErrorMessage.MOVIE_OPEN_API_HISTORY_EMPTY);
        }
        MovieOpenApiHistory movieOpenApiHistory = movieOpenApiHistories.get(0);
        LocalDate startDateInDb = movieOpenApiHistory.getStartDate();
        LocalDate endDateInDb = movieOpenApiHistory.getEndDateDaily();
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

    public FindMovieRankLineChartResponseDtos findMovieRankLineChartByMoviesId(long moviesId) {
        List<MovieRank> movieRanks = movieRankRepository.findMovieRankByMoviesId(moviesId);
        if (movieRanks.size() == 0) {
            throw new NoSuchElementException(MovieRankErrorMessage.INVALID_MOVIES_ID);
        }
        LocalDate startDate = movieRanks.get(0)
                .getMovieRankId()
                .getDate();
        LocalDate endDate = movieRanks.get(movieRanks.size() - 1)
                .getMovieRankId()
                .getDate();
        return lineChartService.toFindMovieRankLineChartResponseDtos(movieRanks, startDate, endDate);
    }

    public FindMovieRankPieChartResponseDtos findMovieRankPieChart(FindMovieRankPieChartRequestDto findMovieRankPieChartRequestDto) {
        LocalDate startDate = findMovieRankPieChartRequestDto.getStartDate();
        LocalDate endDate = findMovieRankPieChartRequestDto.getEndDate();
        validateDate(startDate, endDate);
        List<MovieRank> movieRanks = movieRankRepository.findMovieRankByDate(startDate, endDate);
        return pieChartService.toFindMovieRankPieChartResponseDtos(movieRanks);
    }
}