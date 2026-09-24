package br.com.fiap.validator_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "mining.exchange";
    public static final String QUEUE = "mining.queue";
    public static final String ROUTING_KEY = "mining.command";

    @Bean
    public DirectExchange miningExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue miningQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(miningQueue()).to(miningExchange()).with(ROUTING_KEY);
    }

}