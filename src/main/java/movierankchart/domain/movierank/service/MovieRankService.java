package movierankchart.domain.movierank.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movierank.constants.MovieRankErrorMessage;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.dto.FindMovieRankResponseDto;
import movierankchart.domain.movierank.dto.FindMovieRankResponseDtos;
import movierankchart.domain.movierank.dto.FindMovieRequestDto;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movierank.repository.MovieRankRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieRankService {
    private final MovieRankRepository movieRankRepository;

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

    public FindMovieRankResponseDtos findMovieRank(FindMovieRequestDto findMovieRequestDto) {
        List<MovieRank> movieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(findMovieRequestDto.getDate(), findMovieRequestDto.getMovieRankType());
        if (movieRanks.size() == 0) {
            throw new NoSuchElementException(MovieRankErrorMessage.MOVIE_RANK_NOT_FOUND_DATE);
        }
        List<FindMovieRankResponseDto> findMovieRankResponseDtos = movieRanks.stream()
                .map(MovieRank::toFindMovieRankResponseDto)
                .collect(Collectors.toList());
        return new FindMovieRankResponseDtos(findMovieRankResponseDtos);
    }
}
