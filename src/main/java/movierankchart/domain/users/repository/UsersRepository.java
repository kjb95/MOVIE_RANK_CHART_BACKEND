package movierankchart.domain.users.repository;

import movierankchart.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Long countUsersByMovies_MoviesId(long moviesId);

    List<Users> findUsersByMovies_MoviesId(long moviesId);

    boolean existsUsersByNickname(String nickname);

    Optional<Users> findUsersByNickname(String nickname);
}
