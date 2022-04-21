package com.amonnuns.doorman.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class MessagingRabbitmq {

    private final AmqpAdmin amqpAdmin;

    static final String topicExchangeName = "notifyNODE";
    static final String routingKey = "cerberus/news";
    static final String queueName = "cerberus";

    public MessagingRabbitmq(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }


    private Queue queue(String nomeFila){
        return new Queue(nomeFila,false);
    }

    private TopicExchange exchange(){
        return new TopicExchange(topicExchangeName);
    }

    private Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @PostConstruct
    private void configureRabbitmq(){
        Queue cerberusQueue = queue(queueName);
        TopicExchange notifyNODE = exchange();
        Binding mybinding = binding(cerberusQueue, notifyNODE);

        amqpAdmin.declareQueue(cerberusQueue);
        amqpAdmin.declareExchange(notifyNODE);
        amqpAdmin.declareBinding(mybinding);

    }


}
