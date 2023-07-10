package movierankchart.domain.movierank.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MovieRankId implements Serializable {
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private Integer rank;
}
