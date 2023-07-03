package movierankchart.domain.movieopenapihistory.entity;

import lombok.Getter;
import movierankchart.common.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
public class MovieOpenApiHistory extends AuditEntity {
    @Id
    @GeneratedValue
    private Long movieOpenApiHistoryId;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
}
