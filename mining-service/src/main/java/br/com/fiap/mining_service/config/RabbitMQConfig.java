package br.com.fiap.mining_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // mesmo nome de fila declarado no validator-service
    public static final String QUEUE = "mining.queue";

    @Bean
    public Queue miningQueue() {
        return new Queue(QUEUE, true);
    }

}