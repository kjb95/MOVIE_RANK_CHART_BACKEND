package movierankchart.security.entity;

import lombok.*;
import movierankchart.common.entity.AuditEntity;
import movierankchart.domain.users.entity.Users;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends AuditEntity {
    @Id
    @GeneratedValue
    private Long refreshTokenId;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="users_id", unique = true)
    private Users users;
    @Column(nullable = false)
    private String token;

    public RefreshToken(Users users, String token) {
        this.users = users;
        this.token = token;
    }
}
