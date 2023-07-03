package movierankchart.domain.chatlog.entity;

import lombok.Getter;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.users.entity.Users;

import javax.persistence.*;

@Entity
@Getter
public class ChatLog extends AuditEntity {
    @Id
    @GeneratedValue
    private Long chatLogId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @ManyToOne(optional = false)
    @JoinColumn(name = "users_id")
    private Users users;
    @Column(nullable = false)
    private String contents;
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT b'0'")
    private boolean isDeleted;
}
