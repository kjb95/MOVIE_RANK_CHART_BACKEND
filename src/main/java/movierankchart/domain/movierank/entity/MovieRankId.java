package movierankchart.domain.movierank.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
}
