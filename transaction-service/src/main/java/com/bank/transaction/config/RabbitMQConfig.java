package com.bank.transaction.config;

import org.springframework.amqp.core.*;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DLQ = "transaction.dlq";

    public static final String DLX = "transaction.dlx";

    public static final String DLQ_ROUTING_KEY = "transaction.dlq.routingKey";

    public static final String QUEUE = "transaction.queue";

    public static final String DAILY_INTEREST_QUEUE = "daily.interest.queue";

    public static final String DAILY_INTEREST_ROUTING_KEY = "daily.interest.routingKey";

    public static final String EXCHANGE = "transaction.exchange";

    public static final String ROUTING_KEY = "transaction.routingKey";

    public static final String RESPONSE_QUEUE = "notification.response.queue";

    public static final String RESPONSE_ROUTING_KEY = "notification.response.routingKey";


    @Bean
    public Queue responseQueue() {

        return QueueBuilder
                .durable(RESPONSE_QUEUE)
                .build();
    }

    @Bean
    public Queue queue() {

        return QueueBuilder.durable(QUEUE)
                .withArgument(
                        "x-dead-letter-exchange",
                        DLX
                )
                .withArgument(
                        "x-dead-letter-routing-key",
                        DLQ_ROUTING_KEY
                )
                .build();
    }

    @Bean
    public Queue dailyInterestQueue() {

        return QueueBuilder
                .durable(DAILY_INTEREST_QUEUE)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ);
    }



    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX);
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
    public Binding responseBinding(
            DirectExchange exchange
    ) {

        return BindingBuilder
                .bind(responseQueue())
                .to(exchange)
                .with(RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding(
            Queue deadLetterQueue,
            DirectExchange deadLetterExchange
    ) {

        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public Binding dailyInterestBinding(
            DirectExchange exchange
    ) {

        return BindingBuilder
                .bind(
                        dailyInterestQueue()
                )
                .to(exchange)
                .with(
                        DAILY_INTEREST_ROUTING_KEY
                );
    }

    @Bean
    public MessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
    }
}