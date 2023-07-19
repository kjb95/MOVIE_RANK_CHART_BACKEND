package movierankchart.domain.movieopenapihistory.repository;

import movierankchart.domain.movieopenapihistory.entity.MovieOpenApiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MovieOpenApiHistoryRepository extends JpaRepository<MovieOpenApiHistory, Long> {
    @Query("SELECT m.startDate FROM MovieOpenApiHistory m")
    Optional<LocalDate> findStartDate();

    @Query("SELECT m.endDate FROM MovieOpenApiHistory m")
    Optional<LocalDate> findEndDate();

    @Modifying
    @Transactional
    @Query("UPDATE MovieOpenApiHistory m SET m.startDate = :startDate")
    void updateStartDate(@Param("startDate") LocalDate startDate);

    @Modifying
    @Transactional
    @Query("UPDATE MovieOpenApiHistory m SET m.endDate = :endDate")
    void updateEndDate(@Param("endDate") LocalDate endDate);
}
