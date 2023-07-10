package movierankchart.domain.users.entity;

import lombok.*;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends AuditEntity {
    @Id
    @GeneratedValue
    private Long usersId;
    @ManyToOne
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Column(nullable = false)
    private String nickname;
}
