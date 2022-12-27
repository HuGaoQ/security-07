package com.ncamc.controller;

import com.ncamc.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
 
/**
 * @author ：jerry
 * @date ：Created in 2021/12/29 14:02
 * @description：MQ生产者
 * @version: V1.1
 */
@Slf4j
@RestController
public class RpcClientController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
 
    @GetMapping("/send")
    public String send(String message) {
        // 创建消息对象
        Message newMessage = MessageBuilder.withBody(message.getBytes()).build();
        log.info("生产者发送消息----->>>>>", newMessage);
        //客户端发送消息
        Message result = rabbitTemplate.sendAndReceive(RabbitConfig.RPC_EXCHANGE, RabbitConfig.RPC_QUEUE1, newMessage);
 
        String response = "";
        if (result != null) {
            // 获取已发送的消息的 correlationId
            String correlationId = newMessage.getMessageProperties().getCorrelationId();
            log.info("生产者----->>>>>{}", correlationId);
 
            // 获取响应头信息
            HashMap<String, Object> headers = (HashMap<String, Object>) result.getMessageProperties().getHeaders();
 
            // 获取 server 返回的消息 id
            String msgId = (String) headers.get("spring_returned_message_correlation");
 
            if (msgId.equals(correlationId)) {
                response = new String(result.getBody());
                log.info("生产者发送消息----->>>>>：{}", response);
            }
        }
        return response;
    }
}