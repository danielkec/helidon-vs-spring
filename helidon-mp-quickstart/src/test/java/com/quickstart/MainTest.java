package com.quickstart;

import io.helidon.microprofile.testing.junit5.HelidonTest;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@HelidonTest
class MainTest {

    @Inject
    private WebTarget target;

    @Test
    void testGreet() {
        String message = target
                .path("proxy-greet")
                .request()
                .get(String.class);
        assertThat(message, is("Greetings from Helidon MP! Sleepy greeting called 200 times."));
    }
}
