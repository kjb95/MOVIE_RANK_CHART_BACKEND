package movierankchart.domain.movierank.entity;

import lombok.*;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieRankTotalWeekly extends AuditEntity {
    @EmbeddedId
    private MovieRankId movieRankTotalWeeklyId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Embedded
    private MovieRank movieRank;
}
