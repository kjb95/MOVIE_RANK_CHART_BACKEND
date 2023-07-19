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

    @Query("SELECT m from MovieRank m WHERE m.movieRankId.date = :date AND m.movieRankId.movieRankType = :movieRankType")
    List<MovieRank> findMovieRankByDateAndMovieRankType(@Param("date") LocalDate date, @Param("movieRankType")MovieRankType movieRankType);
}
