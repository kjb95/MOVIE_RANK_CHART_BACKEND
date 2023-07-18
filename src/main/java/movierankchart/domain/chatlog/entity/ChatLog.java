package movierankchart.domain.chatlog.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatLog extends AuditEntity {
    @Id
    @Column
    private long chatLogId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movies_id", referencedColumnName = "chat_log_id")
    private Movies movies;

    @Column(nullable = false)
    private String contents;
}
