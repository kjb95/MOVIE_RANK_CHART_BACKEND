package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.constants.MovieRankType;
import movierankchart.domain.movierank.entity.MovieRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRankRepository extends JpaRepository<MovieRank, Long> {
    @Query("SELECT min(m.movieRankId.date) FROM MovieRank m")
    LocalDate findMinDate();

    @Query("SELECT max(m.movieRankId.date) FROM MovieRank m WHERE m.movieRankId.movieRankType = 'TOTAL_DAILY'")
    LocalDate findEndDateDaily();

    @Query("SELECT max(m.movieRankId.date) FROM MovieRank m WHERE m.movieRankId.movieRankType = 'TOTAL_WEEKLY'")
    LocalDate findEaxDateWeekly();

    @Query("SELECT m FROM MovieRank m LEFT OUTER JOIN FETCH m.movies WHERE m.movieRankId.date = :date AND m.movieRankId.movieRankType = :movieRankType ORDER BY m.movieRankId.rank")
    List<MovieRank> findMovieRankByDateAndMovieRankType(@Param("date") LocalDate date, @Param("movieRankType")MovieRankType movieRankType);

    @Query("SELECT m FROM MovieRank m LEFT OUTER JOIN FETCH m.movies WHERE m.movieRankId.date BETWEEN :startDate AND :endDate AND m.movieRankId.movieRankType = 'TOTAL_DAILY' ORDER BY m.movieRankId.date")
    List<MovieRank> findMovieRankByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT mr FROM MovieRank mr JOIN FETCH mr.movies m WHERE m.moviesId =:moviesId AND mr.movieRankId.movieRankType = 'TOTAL_DAILY' ORDER BY mr.movieRankId.date")
    List<MovieRank> findMovieRankByMoviesId(@Param("moviesId") long moviesId);
}
