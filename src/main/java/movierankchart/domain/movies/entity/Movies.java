package movierankchart.domain.movies.entity;

import lombok.Getter;
import movierankchart.common.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
public class Movies extends AuditEntity {
    @Id
    private Long moviesId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate openingDate;
    @Column(nullable = false)
    private String poster;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String nation;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false)
    private Integer runtime;
    @Column(nullable = false)
    private String ratingGrade;
}
