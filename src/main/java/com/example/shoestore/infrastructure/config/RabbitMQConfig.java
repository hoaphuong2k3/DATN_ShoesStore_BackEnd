//package com.example.shoestore.infrastructure.config;
//
//import com.example.shoestore.core.account.staff.model.response.SendEmail;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import static org.springframework.amqp.core.BindingBuilder.bind;
//
//@Configuration
//@EnableRabbit
//public class RabbitMQConfig {
//
//    @Value("${rabbitmq.exchange.type}")
//    private String directExchangeName;
//
//    @Value("${rabbitmq.routing.client}")//đọc giá trị từ file properties
//    private String routingClient;
//
//    @Value("${rabbitmq.routing.staff}")//đọc giá trị từ file properties
//    private String routingStaff;
//
//    public static final String TOPIC_QUEUE_CLIENT = "topic.queue-1";
//
//    public static final String TOPIC_QUEUE_STAFF = "topic.queue-2";
//
//    @Bean
//    public MessageConverter converter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(converter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public Declarables directBingdings() {
//        Queue directQueueClient = new Queue(TOPIC_QUEUE_CLIENT, false);
//        Queue directQueueStaff = new Queue(TOPIC_QUEUE_STAFF, false);
//        DirectExchange directExchange = new DirectExchange(directExchangeName);
//        return new Declarables(
//                directExchange,
//                bind(directQueueClient).to(directExchange).with(routingClient),
//                bind(directQueueStaff).to(directExchange).with(routingStaff)
//        );
//    }
//}
