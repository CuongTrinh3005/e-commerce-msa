package com.research.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @KafkaListener(topics = "notification-topic")
    private void handleNotification(OrderPublishRecord orderPublishRecord){
        log.info("Get notification for the order: " + orderPublishRecord.getOrderNumber());
    }
}
