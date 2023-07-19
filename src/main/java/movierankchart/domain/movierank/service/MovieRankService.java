package movierankchart.domain.movierank.service;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.entity.MovieRank;
import movierankchart.domain.movierank.repository.MovieRankRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieRankService {
    private final MovieRankRepository movieRankRepository;

    public boolean hasInvalidMovieRankData(List<LocalDate> datesInRange) {
        return datesInRange.stream()
                .filter(this::isInvalidMovieRankData)
                .count() > 0;
    }

    private boolean isInvalidMovieRankData(LocalDate date) {
        List<MovieRank> totalDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.TOTAL_DAILY);
        List<MovieRank> koreanDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.KOREAN_DAILY);
        List<MovieRank> foreignDailyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.FOREIGN_DAILY);
        List<MovieRank> totalWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.TOTAL_WEEKLY);
        List<MovieRank> koreanWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.KOREAN_WEEKLY);
        List<MovieRank> foreignWeeklyMovieRanks = movieRankRepository.findMovieRankByDateAndMovieRankType(date, MovieRankType.FOREIGN_WEEKLY);

        if (totalDailyMovieRanks.size() != 10 || koreanDailyMovieRanks.size() != 10 || foreignDailyMovieRanks.size() != 10) {
            return true;
        }
        if ((date.getDayOfWeek() != DayOfWeek.MONDAY) && (totalWeeklyMovieRanks.size() != 0 || koreanWeeklyMovieRanks.size() != 0 || foreignWeeklyMovieRanks.size() != 0)) {
            return true;
        }
        if ((date.getDayOfWeek() == DayOfWeek.MONDAY) && (totalWeeklyMovieRanks.size() != 10 || koreanWeeklyMovieRanks.size() != 10 || foreignWeeklyMovieRanks.size() != 10)) {
            return true;
        }
        return false;
    }
}
