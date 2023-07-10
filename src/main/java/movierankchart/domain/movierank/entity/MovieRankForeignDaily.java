package movierankchart.domain.movierank.entity;

import lombok.*;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieRankForeignDaily extends AuditEntity {
    @Id
    @GeneratedValue
    private Long movieRankForeignDailyId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Column(nullable = false)
    private LocalDate date;
    @Embedded
    private MovieRank movieRank;
}
