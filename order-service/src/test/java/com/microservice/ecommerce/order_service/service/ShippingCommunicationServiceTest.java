package com.microservice.ecommerce.order_service.service;

import com.microservice.ecommerce.order_service.clients.ShippingOpenFeignClient;
import com.microservice.ecommerce.order_service.dto.ShippingResponseDto;
import io.github.resilience4j.springboot3.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration;
import io.github.resilience4j.springboot3.retry.autoconfigure.RetryAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShippingCommunicationServiceTest {

    private final ShippingCommunicationService shippingService =
            new ShippingCommunicationService(mock(ShippingOpenFeignClient.class));

    @Test
    void fallbackReturnsPendingStatus() {
        ShippingResponseDto result = shippingService.shippingFallback(
                10L,
                new RuntimeException("ShippingService is down")
        );

        assertThat(result.orderId()).isEqualTo(10L);
        assertThat(result.status()).isEqualTo("PENDING");
    }

    @Test
    void retriesBeforeUsingFallbackAndThenOpensTheCircuit() {
        ShippingOpenFeignClient failingClient = mock(ShippingOpenFeignClient.class);
        when(failingClient.confirmShipping(10L))
                .thenThrow(new RuntimeException("ShippingService is down"));

        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        AopAutoConfiguration.class,
                        RetryAutoConfiguration.class,
                        CircuitBreakerAutoConfiguration.class
                ))
                .withPropertyValues(
                        "resilience4j.retry.instances.shippingRetry.max-attempts=3",
                        "resilience4j.retry.instances.shippingRetry.wait-duration=1ms",
                        "resilience4j.circuitbreaker.circuit-breaker-aspect-order=1",
                        "resilience4j.circuitbreaker.instances.shippingCircuitBreaker.sliding-window-size=5",
                        "resilience4j.circuitbreaker.instances.shippingCircuitBreaker.minimum-number-of-calls=3",
                        "resilience4j.circuitbreaker.instances.shippingCircuitBreaker.failure-rate-threshold=50",
                        "resilience4j.circuitbreaker.instances.shippingCircuitBreaker.wait-duration-in-open-state=1h"
                )
                .withBean(
                        ShippingCommunicationService.class,
                        () -> new ShippingCommunicationService(failingClient)
                )
                .run(context -> {
                    ShippingCommunicationService service =
                            context.getBean(ShippingCommunicationService.class);

                    for (int attempt = 0; attempt < 4; attempt++) {
                        ShippingResponseDto result = service.confirmShipping(10L);
                        assertThat(result.status()).isEqualTo("PENDING");
                    }

                    // Three failed calls, each with three attempts. The fourth
                    // call is rejected immediately because the circuit is open.
                    verify(failingClient, times(9)).confirmShipping(10L);
                });
    }
}
