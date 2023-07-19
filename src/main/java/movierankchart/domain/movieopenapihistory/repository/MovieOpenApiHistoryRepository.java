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

    @Query("SELECT m.endDateDaily FROM MovieOpenApiHistory m")
    Optional<LocalDate> findEndDateDaily();

    @Query("SELECT m.endDateWeekly FROM MovieOpenApiHistory m")
    Optional<LocalDate> findEndDateWeekly();
    @Modifying
    @Transactional
    @Query("UPDATE MovieOpenApiHistory m SET m.startDate = :startDate")
    void updateStartDate(@Param("startDate") LocalDate startDate);

    @Modifying
    @Transactional
    @Query("UPDATE MovieOpenApiHistory m SET m.endDateDaily = :endDateDaily")
    void updateEndDateDaily(@Param("endDateDaily") LocalDate endDateDaily);

    @Modifying
    @Transactional
    @Query("UPDATE MovieOpenApiHistory m SET m.endDateWeekly = :endDateWeekly")
    void updateEndDateWeekly(@Param("endDateWeekly") LocalDate endDateWeekly);
}
