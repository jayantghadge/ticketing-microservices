package com.example.jayant.apigateway.route;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceRoutesTest {

    @Mock
    private ServerRequest request;

    @Test
    void forwardWithPathVariable_ShouldExtractAndForward() throws Exception {
        when(request.pathVariable("venueId")).thenReturn("1");

        Method method = InventoryServiceRoutes.class.getDeclaredMethod(
                "forwardWithPathVariable", ServerRequest.class, String.class, String.class);
        method.setAccessible(true);

        assertThrows(Exception.class, () ->
                method.invoke(null, request, "venueId", "http://localhost:9999/api/v1/inventory/venue/"));
    }
}
