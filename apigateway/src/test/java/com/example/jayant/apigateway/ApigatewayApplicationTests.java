package com.example.jayant.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "keycloak.auth.jwk-set-uri=http://localhost:8091/realms/ticketing-security-realm/protocol/openid-connect/certs",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8091/realms/ticketing-security-realm"
})
class ApigatewayApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void main_shouldStart() {
        ApigatewayApplication.main(new String[]{
                "--keycloak.auth.jwk-set-uri=http://localhost:8091/realms/ticketing-security-realm/protocol/openid-connect/certs",
                "--spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8091/realms/ticketing-security-realm"
        });
    }
}
