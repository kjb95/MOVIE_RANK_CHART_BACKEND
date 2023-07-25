package movierankchart.domain.users.repository;

import movierankchart.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Long countUsersByMovies_MoviesId(Long moviesId);
}
