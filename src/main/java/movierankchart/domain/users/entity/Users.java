package movierankchart.domain.users.entity;

import lombok.Getter;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;

@Entity
@Getter
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
