package movierankchart.batch.writer;

import movierankchart.domain.movierank.entity.*;
import movierankchart.domain.movierank.repository.*;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.movies.repository.MoviesRepository;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Component
public class SaveMovieRankWriter extends JpaItemWriter<List<MovieRank>> {
    @Autowired
    private MoviesRepository moviesRepository;
    @Autowired
    private MovieRankRepository movieRankRepository;

    public SaveMovieRankWriter(EntityManagerFactory entityManagerFactory) {
        setEntityManagerFactory(entityManagerFactory);
    }

    @Override
    public void write(List<? extends List<MovieRank>> items) {
        items.forEach(itemList -> itemList.forEach(this::saveMovieRank));
    }

    private void saveMovieRank(MovieRank item) {
        Movies movies = item.getMovies();
        if (movies != null) {
            moviesRepository.save(movies);
        }
        movieRankRepository.save(item);
    }
}
