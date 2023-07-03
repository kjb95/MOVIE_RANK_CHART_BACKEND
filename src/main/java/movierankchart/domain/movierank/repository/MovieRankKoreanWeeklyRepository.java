package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.entity.MovieRankKoreanWeekly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRankKoreanWeeklyRepository extends JpaRepository<MovieRankKoreanWeekly, Long> {
}
