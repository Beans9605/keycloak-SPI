package org.keycloak.custom.storage.dto;

import lombok.Builder;
import org.keycloak.custom.storage.domain.UserSideClientEntity;

public record ClientRole(
    Long clientNo,
    String clientName,
    String desc,
    Long roleNo,
    String roleName
) {
    @Builder
    public ClientRole{}

    public static ClientRole from(UserSideClientEntity usc) {
        return ClientRole.builder()
                .clientNo(usc.getClientNo())
                .clientName(usc.getSideClient().getClientName())
                .desc(usc.getSideClient().getDesc())
                .roleNo(usc.getRoleNo())
                .roleName(usc.getRole().getRoleName())
                .build();
    }
}
