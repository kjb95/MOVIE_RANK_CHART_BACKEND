package movierankchart.domain.movierank.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class MovieRank {
    @Column(nullable = false)
    private Integer rank;
    @Column(nullable = false)
    private Integer rankIncrement;
    @Column(columnDefinition = "tinyint(1)", nullable = false)
    private boolean isNewRank;
    @Column(nullable = false)
    private Long audienceNum;
    @Column(nullable = false)
    private Long sales;
    @Column(nullable = false)
    private Long screeningsNum;
    @Column(nullable = false)
    private Long theatersNum;
}
