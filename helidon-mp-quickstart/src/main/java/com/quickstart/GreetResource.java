package com.quickstart;

import java.util.ArrayList;
import java.util.List;

import io.helidon.microprofile.server.ServerCdiExtension;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

@Path("/")
@ApplicationScoped
public class GreetResource {

    private final WebTarget webTarget;

    @Inject
    public GreetResource(ServerCdiExtension server) {
        this.webTarget = ClientBuilder.newClient().target("http://localhost:" + server.port());
    }

    @GET
    @Path("/sleep-greet")
    public String sleepGreet() throws InterruptedException {
        Thread.sleep(100);
        return "Greetings from sleepy Helidon MP!";
    }

    @GET
    @Path("/proxy-greet")
    public String proxyGreet() throws InterruptedException {
        List<Thread> threads = new ArrayList<>(100);
        for (int i = 0; i < 200; i++) {
            threads.add(Thread.ofVirtual().start(() -> webTarget.path("/sleep-greet").request().get(String.class)));
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return "Greetings from Helidon MP! Sleepy greeting called " + threads.size() + " times.";
    }

}
