/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.custom.storage.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.*;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Setter
@Getter
@NamedQueries({
        @NamedQuery(name="getUserByUsername", query="select u from UserEntity u where u.username = :username"),
        @NamedQuery(name="getUserByEmail", query="select u from UserEntity u where u.email = :email"),
        @NamedQuery(name="getUserCount", query="select count(u) from UserEntity u"),
        @NamedQuery(name="getAllUsers", query="select u from UserEntity u"),
        @NamedQuery(name="searchForUser", query="select u from UserEntity u where " +
                "( lower(u.username) like :search or u.email like :search ) order by u.username"),
})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private String id;


    private String username;
    private String email;
    private String password;
    private String phone;

}
