package movierankchart.domain.users.entity;

import lombok.*;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.users.dto.response.FindUsersInChatRoomResponseDto;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends AuditEntity {
    @Id
    @GeneratedValue
    private Long usersId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Column(nullable = false)
    private String nickname;

    public FindUsersInChatRoomResponseDto toFindUsersInChatRoomResponseDto() {
        return new FindUsersInChatRoomResponseDto(usersId, nickname);
    }
}
