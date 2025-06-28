package org.example.diplomicproject.testConfig;

import org.example.diplomicproject.service.CartService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public CartService cartService() {
        return Mockito.mock(CartService.class);
    }
}
