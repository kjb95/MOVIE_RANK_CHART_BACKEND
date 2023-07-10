package movierankchart.domain.movierank.entity;

import lombok.*;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieRankKoreanDaily extends AuditEntity {
    @EmbeddedId
    private MovieRankId movieRankKoreanDailyId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Embedded
    private MovieRank movieRank;
}
