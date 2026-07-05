package com.example.jayant.apigateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
        "keycloak.auth.jwk-set-uri=http://localhost:8091/realms/ticketing-security-realm/protocol/openid-connect/certs",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8091/realms/ticketing-security-realm"
})
class SecurityConfigTest {

    @Autowired(required = false)
    private SecurityFilterChain securityFilterChain;

    @Autowired(required = false)
    private JwtDecoder jwtDecoder;

    @Test
    void securityFilterChain_ShouldBeConfigured() {
        assertNotNull(securityFilterChain);
        assertNotNull(jwtDecoder);
    }
}
