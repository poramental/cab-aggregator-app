package com.modsen.rideservice.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainerConfigJunit5 {
    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");


    public static void run(){
        container.start();
    }
}
