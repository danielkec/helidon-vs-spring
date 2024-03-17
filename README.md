# Helidon vs. Spring Boot
## Thread-Pool depletion example
### Prerequisites
 * Maven
 * JDK >= 21
 * Machine with at-least 16 physical CPU cores

### Abstract
This example demonstrate the situation when webserver is accessed by more than 200 
concurrent HTTP requests (which is more than default thread-pool size in Spring Boot).

As such use-case is complicated to do on single machine with less than 200 CPUs,
HTTP requests are multiplied in the `/proxy-greet` handler 200-fold against `/sleep-greet` 
handler which blocks the client for 200 millis.

While handlers powered by virtual threads don't block resources, 
pooled threads in Spring Boot are quickly depleted and performance degrades.  

### Build
```bash
mvn package
```

### Test Spring Boot variant
Run Spring Boot app:
```bash
java -jar ./spring-boot-quickstart/target/spring-boot-quickstart-1.0-SNAPSHOT.jar
```

Execute 30 sec load test on 30 parallel connections with [Wrk tool](https://github.com/wg/wrk) against `/proxy-greet`
```bash
wrk -c 16 -t 16 -d 30s http://localhost:8080/proxy-greet
```
Stop running Spring Boot app.

### Test Helidon 4 variant
Start Helidon 4 MP variant:
```bash
java -jar ./helidon-mp-quickstart/target/helidon-mp-quickstart.jar
```

Execute 30 sec load test on 30 parallel connections with [Wrk tool](https://github.com/wg/wrk) against `/proxy-greet`
```bash
wrk -c 16 -t 16 -d 30s http://localhost:8080/proxy-greet
```
Stop running Helidon MP app.

