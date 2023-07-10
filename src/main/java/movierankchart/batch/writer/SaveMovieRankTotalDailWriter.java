package movierankchart.batch.writer;

import movierankchart.domain.movierank.entity.MovieRankTotalDaily;
import movierankchart.domain.movierank.repository.MovieRankTotalDailyRepository;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.movies.repository.MoviesRepository;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Component
public class SaveMovieRankTotalDailWriter extends JpaItemWriter<List<MovieRankTotalDaily>> {
    @Autowired
    private MovieRankTotalDailyRepository movieRankTotalDailyRepository;
    @Autowired
    private MoviesRepository moviesRepository;


    public SaveMovieRankTotalDailWriter(EntityManagerFactory entityManagerFactory) {
        setEntityManagerFactory(entityManagerFactory);
    }

    @Override
    public void write(List<? extends List<MovieRankTotalDaily>> items) {
        items.forEach(itemList -> itemList.forEach(this::saveMovieRankTotalDaily));
    }

    private void saveMovieRankTotalDaily(MovieRankTotalDaily item) {
        Movies movies = item.getMovies();
        moviesRepository.save(movies);
        movieRankTotalDailyRepository.save(item);
    }
}
