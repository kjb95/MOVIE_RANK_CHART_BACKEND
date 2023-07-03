package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.entity.MovieRankTotalWeekly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRankTotalWeeklyRepository extends JpaRepository<MovieRankTotalWeekly, Long> {
}
