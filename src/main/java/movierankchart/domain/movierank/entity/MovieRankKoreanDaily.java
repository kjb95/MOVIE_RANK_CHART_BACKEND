package movierankchart.domain.movierank.entity;

import lombok.Getter;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class MovieRankKoreanDaily extends AuditEntity {
    @Id
    @GeneratedValue
    private Long movieRankKoreanDailyId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Column(nullable = false)
    private LocalDate date;
    @Embedded
    private MovieRank movieRank;
}
