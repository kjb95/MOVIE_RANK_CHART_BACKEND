package movierankchart.domain.users.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.movies.entity.Movies;
import movierankchart.domain.users.dto.response.FindUsersInChatRoomResponseDto;
import movierankchart.domain.users.dto.response.FindUsersResponseDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends AuditEntity implements UserDetails {
    @Id
    @GeneratedValue
    private Long usersId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movies_id")
    private Movies movies;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String picture;

    public static Users createUsers(String email, String name, String picture) {
        Users users = new Users();
        users.email = email;
        users.name = name;
        users.picture = picture;
        return users;
    }

    public FindUsersInChatRoomResponseDto toFindUsersInChatRoomResponseDto() {
        return new FindUsersInChatRoomResponseDto(usersId, email);
    }

    public FindUsersResponseDto toFindUsersResponseDto() {
        return new FindUsersResponseDto(usersId, email, name, picture);
    }

    public void setMovies(Movies movies) {
        this.movies = movies;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
