
package io.helidon.examples.quickstart.mp;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
class MainTest {

    @Inject
    private WebTarget target;

    @Test
    void testHelloWorld() {
        JsonObject jsonObject = target
                .path("user/login")
                .request()
                .get(JsonObject.class);      
    }
}
