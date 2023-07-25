package movierankchart.domain.movies.repository;

import movierankchart.domain.movies.entity.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesRepository extends JpaRepository<Movies, Long> {
    Optional<Movies> findMoviesByTitleAndOpeningDate(String title, LocalDate openingDate);
    List<Movies> findMoviesByTitleContaining(String title);

    @Query("SELECT m FROM Movies m JOIN Users u ON m.moviesId = u.movies.moviesId WHERE m.title LIKE '%:title%'")
    List<Movies> findMoviesByTitleContainingSomeoneInChatRoom(String title);
}
