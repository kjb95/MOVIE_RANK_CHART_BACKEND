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
public class SaveMovieRankWriter extends JpaItemWriter<List<MovieRankBase>> {
    @Autowired
    private MoviesRepository moviesRepository;
    @Autowired
    private MovieRankTotalDailyRepository movieRankTotalDailyRepository;
    @Autowired
    private MovieRankKoreanDailyRepository movieRankKoreanDailyRepository;
    @Autowired
    private MovieRankForeignDailyRepository movieRankForeignDailyRepository;
    @Autowired
    private MovieRankTotalWeeklyRepository movieRankTotalWeeklyRepository;
    @Autowired
    private MovieRankKoreanWeeklyRepository movieRankKoreanWeeklyRepository;
    @Autowired
    private MovieRankForeignWeeklyRepository movieRankForeignWeeklyRepository;

    public SaveMovieRankWriter(EntityManagerFactory entityManagerFactory) {
        setEntityManagerFactory(entityManagerFactory);
    }

    @Override
    public void write(List<? extends List<MovieRankBase>> items) {
        items.forEach(itemList -> itemList.forEach(this::saveMovieRank));
    }

    private void saveMovieRank(MovieRankBase item) {
        Movies movies = item.getMovies();
        moviesRepository.findById(movies.getMoviesId())
                .orElseGet(() -> moviesRepository.save(movies));
        if (item instanceof MovieRankTotalDaily) {
            movieRankTotalDailyRepository.save((MovieRankTotalDaily) item);
        } else if (item instanceof MovieRankKoreanDaily) {
            movieRankKoreanDailyRepository.save((MovieRankKoreanDaily) item);
        }
        else if (item instanceof MovieRankForeignDaily) {
            movieRankForeignDailyRepository.save((MovieRankForeignDaily) item);
        }
        else if (item instanceof MovieRankTotalWeekly) {
            movieRankTotalWeeklyRepository.save((MovieRankTotalWeekly) item);
        }
        else if (item instanceof MovieRankKoreanWeekly) {
            movieRankKoreanWeeklyRepository.save((MovieRankKoreanWeekly) item);
        }
        else if (item instanceof MovieRankForeignWeekly) {
            movieRankForeignWeeklyRepository.save((MovieRankForeignWeekly) item);
        }
    }


}
