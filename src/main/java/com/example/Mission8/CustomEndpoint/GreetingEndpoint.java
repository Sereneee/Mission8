package com.example.Mission8.CustomEndpoint;

import com.example.Mission8.Service.PetService;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Endpoint(id = "hello-endpoint")
public class GreetingEndpoint {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private PetService petService;

    @ReadOperation // equivalent to HTTP Get method
    public Greeting sayHello(@RequestParam(defaultValue = "Random person") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
    @WriteOperation // equivalent to POST method
    public Greeting writeHello(@RequestParam(defaultValue = "Random person") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
    @DeleteOperation// equivalent to HTTP DELETE operation
    public Greeting deleteHello(@RequestParam(defaultValue = "Random person") String name) {
        return new Greeting(counter.decrementAndGet(), String.format(template, name));
    }
}
