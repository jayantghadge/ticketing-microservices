package com.example.jayant.apigateway.route;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
        "keycloak.auth.jwk-set-uri=http://localhost:8091/realms/ticketing-security-realm/protocol/openid-connect/certs",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8091/realms/ticketing-security-realm"
})
class GatewayRoutesTest {

    @Autowired(required = false)
    private RouterFunction<ServerResponse> bookingRoutes;

    @Autowired(required = false)
    private RouterFunction<ServerResponse> bookingServiceApiDocs;

    @Autowired(required = false)
    private RouterFunction<ServerResponse> inventoryRoutes;

    @Autowired(required = false)
    private RouterFunction<ServerResponse> inventoryServiceApiDocs;

    @Test
    void allRoutes_ShouldBeConfigured() {
        assertNotNull(bookingRoutes, "bookingRoutes bean should exist");
        assertNotNull(bookingServiceApiDocs, "bookingServiceApiDocs bean should exist");
        assertNotNull(inventoryRoutes, "inventoryRoutes bean should exist");
        assertNotNull(inventoryServiceApiDocs, "inventoryServiceApiDocs bean should exist");
    }
}
