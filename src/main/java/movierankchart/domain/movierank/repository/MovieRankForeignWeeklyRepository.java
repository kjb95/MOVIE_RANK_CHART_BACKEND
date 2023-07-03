package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.entity.MovieRankForeignWeekly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRankForeignWeeklyRepository extends JpaRepository<MovieRankForeignWeekly, Long> {
}
