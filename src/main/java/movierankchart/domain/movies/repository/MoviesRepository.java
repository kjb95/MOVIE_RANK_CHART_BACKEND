package movierankchart.domain.movies.repository;

import movierankchart.domain.movies.entity.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MoviesRepository extends JpaRepository<Movies, Long> {
    Optional<Movies> findMoviesByTitleAndOpeningDate(String title, LocalDate openingDate);
}
