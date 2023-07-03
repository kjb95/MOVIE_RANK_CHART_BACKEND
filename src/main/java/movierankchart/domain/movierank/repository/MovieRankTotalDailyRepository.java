package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.entity.MovieRankTotalDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRankTotalDailyRepository extends JpaRepository<MovieRankTotalDaily, Long> {
}
