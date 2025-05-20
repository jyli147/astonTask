package com.example.mod3.config;

import com.example.mod3.Dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class KafkaProducer {

        private final KafkaTemplate<String, String> kafkaTemplate;
        private final ObjectMapper objectMapper;

        @Value("${spring.kafka.topic.user-topic}")
        private String userTopic;

        public void sendMessage(MessageDto messageDto) {
            try {
                String message = objectMapper.writeValueAsString(messageDto);
                kafkaTemplate.send(userTopic, message);
            } catch (Exception e) {
                log.error("Error sending message to Kafka! {}", e.getMessage());
                throw new RuntimeException("Error sending message to Kafka!", e);
            }
        }
    }