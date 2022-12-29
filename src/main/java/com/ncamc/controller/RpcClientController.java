package com.ncamc.controller;

import com.ncamc.config.JwtProperties;
import com.ncamc.config.RabbitConfig;
import com.ncamc.service.LoginService;
import com.ncamc.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api("消息生产")
@RestController
public class RpcClientController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtProperties jwtProperties;

    public static final String LOGIN_TOKEN = "login_token:";
    public static final String LOGIN_USER = "login_user:";

    @ApiOperation("发送用户信息")
    @GetMapping("/send")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataTypeClass = Integer.class, example = "ID"),
            @ApiImplicitParam(name = "message", value = "要发送的内容", required = true, dataTypeClass = String.class, example = "要发送的内容")
    })
    public String send(Long id,String message) {
        //获取用户信息
        String user = String.valueOf(loginService.getById(id));
        // 创建消息对象
        Message newMessage = MessageBuilder.withBody(user.getBytes()).build();
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