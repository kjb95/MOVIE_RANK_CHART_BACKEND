package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.entity.MovieRankKoreanDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRankKoreanDailyRepository extends JpaRepository<MovieRankKoreanDaily, Long> {
}
