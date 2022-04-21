package com.amonnuns.doorman.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitNotify {

    private final RabbitTemplate rabbitTemplate;

    private static final String NOME_EXCHANGE = "notifyNODE";
    private static final String ROUTING_KEY =  "cerberus/news";

    public RabbitNotify(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setExchange(NOME_EXCHANGE);
        rabbitTemplate.setRoutingKey(ROUTING_KEY);
    }

    public void enviarNotificacao(String message){
        rabbitTemplate.send(new Message(message.getBytes()));
    }

}
