package org.keycloak.custom.storage.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SideClientEntity {
    @Id
    @Column(name = "client_no")
    private Long clientNo;
    @Column(name = "client_name")
    private String clientName;
    private String desc;
}
