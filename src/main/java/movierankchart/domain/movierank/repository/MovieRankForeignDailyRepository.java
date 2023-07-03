package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.entity.MovieRankForeignDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRankForeignDailyRepository extends JpaRepository<MovieRankForeignDaily, Long> {
}
