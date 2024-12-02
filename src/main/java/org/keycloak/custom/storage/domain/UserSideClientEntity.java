package org.keycloak.custom.storage.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@NamedQueries({
        @NamedQuery(name="getUserSideClientByUserId", query = """
        select usc from UserSideClientEntity usc
        left join usc.sideClient sc left join usc.role r
        where usc.userId = :userId
        """)
})
@IdClass(UserSideClientEntity.UserSideClientID.class)
public class UserSideClientEntity {
    @Id
    @Column(name = "client_no")
    private Long clientNo;
    @Id
    @Column(name = "user_id")
    private String userId;
    private Long roleNo;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(targetEntity = SideClientEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_no", referencedColumnName = "client_no", insertable = false, updatable = false)
    private SideClientEntity sideClient;

    @ManyToOne(targetEntity = RoleEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_no", referencedColumnName = "role_no", insertable = false, updatable = false)
    private RoleEntity role;

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class UserSideClientID {
        private Long clientNo;
        private String userId;
    }
}
