package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.entity.MovieRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MovieRankRepository extends JpaRepository<MovieRank, Long> {
    @Query("SELECT min(m.movieRankId.date) FROM MovieRank m")
    LocalDate findMinDate();
}
