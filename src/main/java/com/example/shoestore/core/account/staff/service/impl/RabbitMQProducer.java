//package com.example.shoestore.core.account.staff.service.impl;
//
//import com.example.shoestore.core.account.staff.model.response.SendEmail;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RabbitMQProducer {
//
//    @Value("${rabbitmq.exchange.type}")//đọc giá trị từ file properties
//    private String directExchangeName;
//
//    @Value("${rabbitmq.routing.client}")//đọc giá trị từ file properties
//    private String routingClient;
//
//    @Value("${rabbitmq.routing.staff}")//đọc giá trị từ file properties
//    private String routingStaff;
//
//    @Bean
//    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
//        return new MappingJackson2MessageConverter();
//    }
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);//Tạo 1 lịch trình ghi nhật ký
//
//    private RabbitTemplate rabbitTemplate;
//
//    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void sendEmailClient(SendEmail sendEmail) {
//        LOGGER.info(String.format("Message sent -> %s", sendEmail));
//        rabbitTemplate.convertAndSend(directExchangeName, routingClient,
//                sendEmail);
//    }
//
//    public void sendEmailStaff(SendEmail sendEmail) {
//        LOGGER.info(String.format("Message sent -> %s", sendEmail));
//        rabbitTemplate.convertAndSend(directExchangeName, routingStaff,
//                sendEmail);
//    }
//
//}
