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
public class MovieRankKoreanWeekly extends AuditEntity {
    @Id
    @GeneratedValue
    private Long movieRankKoreanWeeklyId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Embedded
    private MovieRank movieRank;
}
