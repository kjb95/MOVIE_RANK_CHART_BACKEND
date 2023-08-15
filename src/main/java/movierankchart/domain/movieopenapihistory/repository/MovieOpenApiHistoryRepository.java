package movierankchart.domain.movieopenapihistory.repository;

import movierankchart.domain.movieopenapihistory.entity.MovieOpenApiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieOpenApiHistoryRepository extends JpaRepository<MovieOpenApiHistory, Long> {
}
