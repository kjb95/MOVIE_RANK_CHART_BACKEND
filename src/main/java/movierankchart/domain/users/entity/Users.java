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
    @Column(nullable = false, unique = true)
    private String nickname;

    public static Users createUsers(String nickname) {
        Users users = new Users();
        users.nickname = nickname;
        return users;
    }

    public FindUsersInChatRoomResponseDto toFindUsersInChatRoomResponseDto() {
        return new FindUsersInChatRoomResponseDto(usersId, nickname);
    }

    public void setMovies(Movies movies) {
        this.movies = movies;
    }
}
