package com.bank.transaction.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE =
            "bank.exchange";

    public static final String QUEUE =
            "deposit.queue";

    public static final String ROUTING_KEY =
            "deposit.routing.key";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(
            Queue queue,
            DirectExchange exchange
    ) {

        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {

        return new JacksonJsonMessageConverter();
    }


    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {

        RabbitTemplate rabbitTemplate =
                new RabbitTemplate(
                        connectionFactory
                );

        rabbitTemplate.setMessageConverter(
                messageConverter
        );

        return rabbitTemplate;
    }
}