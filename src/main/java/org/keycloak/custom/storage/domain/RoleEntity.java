package org.keycloak.custom.storage.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RoleEntity {
    @Id
    @Column(name = "role_no")
    private Long roleNo;
    @Column(name = "role_name")
    private String roleName;
    private LocalDateTime created;
}
