package movierankchart.domain.movierank.entity;

import lombok.*;
import movierankchart.domain.movierank.constants.MovieRankType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MovieRankId implements Serializable {
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private Integer rank;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovieRankType movieRankType;
}
