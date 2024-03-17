package com.example.springquickstart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GreetController {

    private RestTemplate restTemplate;

    @EventListener
    public void onApplicationEvent(final ServletWebServerInitializedEvent e) {
        var port = e.getWebServer().getPort();
        this.restTemplate = new RestTemplateBuilder().rootUri("http://localhost:" + port).build();
    }

    @GetMapping("/sleep-greet")
    public String sleepGreet() throws InterruptedException {
        Thread.sleep(100);
        return "Greetings from sleepy Spring Boot!";
    }

    @GetMapping("/proxy-greet")
    public String proxy() throws InterruptedException {
        List<Thread> threads = new ArrayList<>(100);
        for (int i = 0; i < 200; i++) {
            threads.add(Thread.ofVirtual().start(() -> restTemplate.getForObject("/sleep-greet", String.class)));
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return "Greetings from Spring Boot! Sleepy greeting called " + threads.size() + " times.";
    }

}