package movierankchart.domain.movierank.entity;

import lombok.Getter;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.kmdb.dto.KmdbResultResponseDto;
import movierankchart.domain.kobis.dto.KobisBoxOfficeResponseDto;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MovieRankBase extends AuditEntity {
    @EmbeddedId
    protected MovieRankId movieRankId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movies_id")
    protected Movies movies;
    @Embedded
    protected MovieRankStatistics movieRankStatistics;

    public abstract MovieRankBase createMovieRank(String showRange, KobisBoxOfficeResponseDto kobisBoxOfficeResponseDto, KmdbResultResponseDto kmdbResultResponseDto);
}
