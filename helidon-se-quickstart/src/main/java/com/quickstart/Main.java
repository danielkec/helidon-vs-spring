package com.quickstart;

import java.util.ArrayList;
import java.util.List;

import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webclient.api.WebClient;
import io.helidon.webserver.WebServer;

public class Main {

    public static void main(String[] args) {
        LogConfig.configureRuntime();
        Config config = Config.create();
        Config.global(config);
        WebClient client = WebClient.create();
        WebServer server = WebServer.builder()
                .config(config.get("server"))
                .routing(r -> r
                        .get("/sleep-greet", (req, res) -> {
                            Thread.sleep(100);
                            res.send("Greetings from sleepy Helidon SE!");
                        })
                        .get("/proxy-greet", (req, res) -> {
                            List<Thread> threads = new ArrayList<>(100);
                            for (int i = 0; i < 200; i++) {
                                threads.add(Thread.ofVirtual().start(() -> client.get("http://localhost:" + req.localPeer()
                                        .port() + "/sleep-greet").requestEntity(String.class)));
                            }
                            for (Thread thread : threads) {
                                thread.join();
                            }
                            res.send("Greetings from Helidon SE! Sleepy greeting called " + threads.size() + " times.");
                        })
                )
                .build()
                .start();

        System.out.println("WEB server is up! http://localhost:" + server.port() + "/proxy-greet");
    }

}