package com.electricity_procject.logic.config;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeycloakConfig {
    static Keycloak keycloak = null;
    public static String serverUrl = "http://localhost:9000"; //temporary
//    public static String serverUrl = "http://keycloak:9000"; //in deployment
    public static String realm = "master";
    static String clientId = "admin-cli";
    static String clientSecret = "G1ghnDccenhUc6QgBQFVNDRkPylqabGW";
    static String userName = "admin";
    static String password = "password";

    public KeycloakConfig() {

    }

    public static Keycloak getInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilderImpl()
                            .connectionPoolSize(10)
                            .build())
                    .build();
        }
        return keycloak;
    }
}



