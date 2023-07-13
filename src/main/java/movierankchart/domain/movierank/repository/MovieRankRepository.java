package movierankchart.domain.movierank.repository;

import movierankchart.domain.movierank.entity.MovieRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRankRepository extends JpaRepository<MovieRank, Long> {
}
