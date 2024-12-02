package org.keycloak.custom.storage.mapper;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.custom.storage.domain.UserSideClientEntity;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.IDToken;
import org.keycloak.storage.StorageId;

import java.util.ArrayList;
import java.util.List;

public class GroupExampleProtocolMapper extends AbstractOIDCProtocolMapper
    implements OIDCIDTokenMapper, OIDCAccessTokenMapper, UserInfoTokenMapper
{
    private final Logger logger = Logger.getLogger(GroupExampleProtocolMapper.class);

    public static final String PROVIDER_ID = "group-example";
    public static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, GroupExampleProtocolMapper.class);
    }

    @Override
    public String getDisplayCategory() {
        return "";
    }

    @Override
    public String getDisplayType() {
        return "";
    }

    @Override
    public String getHelpText() {
        return "";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    protected String getUserByUserSessionModel (UserSessionModel userSessionModel) {
        String id = userSessionModel.getUser().getId();

        logger.debug("--------------------------------------------------");
        logger.debug("getUserById: " + id);

        // SYSTEM_ADMIN
        String SysAdminNo = "";
        if (id != null && id.contains(":")){
            SysAdminNo = id.substring(id.lastIndexOf(":") + 1);
        } else {
            logger.warn("could not find id: " + id);
            return null;
        }

        if (SysAdminNo.equals("0")){
            return null;
        }

        return StorageId.externalId(id);
    }

    /**
     * <p>
     *     UserEntity 에 선언된 primary key 를 기준으로 keycloak 에서 StorageId 값을 만들어 고유값을 유지하고 있음.<br/>
     *     따라서 이 값을 추출하여, userEntity 가 가진 sideClient 들의 role 에 대한 정보를 합쳐 클라이언트 들이 사용할 groups, {@code List<String>}<br/>
     *     값을 만들어주는 method
     * </p>
     *
     * @param session {@link KeycloakSession}
     * @param userSession {@link UserSessionModel}
     * @return {@code List<String>}
     */
    @Transactional
    protected List<String> returnClientGroups(KeycloakSession session, UserSessionModel userSession) {
        EntityManager em = session.getProvider(JpaConnectionProvider.class, "user-store").getEntityManager();
        String userId = this.getUserByUserSessionModel(userSession);
        if (userId != null) {
            List<UserSideClientEntity> uscs = em.createNamedQuery("getUserSideClientByUserId", UserSideClientEntity.class)
                    .setParameter("userId", userId)
                    .getResultList();

            return uscs.stream().map((usc) -> usc.getClientNo() + "::" + usc.getRoleNo()).toList();
        }
        return new ArrayList<>();
    }

    /**
     * @implNote <p>
     *     AccessToken 을 만들 때, token 을 복호화했을때 Claim 값에 사용자가 원하는 값을 추가할 수 있도록 세팅해주는 메소드,<br/>
     *     UserInfo, 즉 token 을 복호화하여 정보를 추출하는 open-id api 를 사용할 때도 값을 추가해준다.
     * </p>
     *
     * @param token {@link AccessTokenResponse}
     * @param mappingModel {@link ProtocolMapperModel}
     * @param userSession {@link UserSessionModel}
     * @param keycloakSession {@link KeycloakSession}
     * @param clientSessionCtx {@link ClientSessionContext}
     */
    @Override
    protected void setClaim(AccessTokenResponse token, ProtocolMapperModel mappingModel,
                            UserSessionModel userSession, KeycloakSession keycloakSession,
                            ClientSessionContext clientSessionCtx) {
        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, returnClientGroups(keycloakSession, userSession));
    }

    /**
     *@implNote <p>
     *     IDToken 을 만들 때, token 을 복호화했을때 Claim 값에 사용자가 원하는 값을 추가할 수 있도록 세팅해주는 메소드,<br/>
     *     </p>
     * @param token {@link IDToken}
     * @param mappingModel {@link ProtocolMapperModel}
     * @param userSession {@link UserSessionModel}
     * @param keycloakSession {@link KeycloakSession}
     * @param clientSessionCtx {@link ClientSessionContext}
     */
    @Override
    protected void setClaim(IDToken token, ProtocolMapperModel mappingModel,
                            UserSessionModel userSession, KeycloakSession keycloakSession,
                            ClientSessionContext clientSessionCtx) {
        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, returnClientGroups(keycloakSession, userSession));
    }
}
