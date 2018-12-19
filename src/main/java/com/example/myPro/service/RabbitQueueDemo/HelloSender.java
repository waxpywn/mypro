package com.example.myPro.service.RabbitQueueDemo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    /**
     * @Author andi
     * @Date: 2018/12/18 18:42:51
     * @Description : rabbitmq简单队列发送者
     */
    public void send() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }
}
