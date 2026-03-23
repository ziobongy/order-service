package it.adesso.management.ordermanagementservice.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;INIT=CREATE SCHEMA IF NOT EXISTS external",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "ANAG_SERVICE_BASE_PATH=http://localhost:8082",
    "spring.rabbitmq.host=localhost",
    "spring.rabbitmq.port=5672",
    "spring.rabbitmq.username=user",
    "spring.rabbitmq.password=password"
})
class WebClientConfigTest {

    @Autowired
    private WebClient webClient;


    @Test
    void testWebClientBeanCreated() {
        // Assert
        assertNotNull(webClient);
    }

    @Test
    void testWebClientCanBeUsed() {
        // Assert
        assertNotNull(webClient.get());
    }
}

