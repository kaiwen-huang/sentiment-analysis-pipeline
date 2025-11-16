// English comments only
package com.sa.web;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

  // ----- Producer -----
  @Bean
  public ProducerFactory<String, String> producerFactory() {
    // Java 8 compatible map build
    Map<String, Object> props = new HashMap<String, Object>();
    // read from env KAFKA_BOOTSTRAP or default to kafka-broker:9092
    String bootstrap = System.getenv("KAFKA_BOOTSTRAP") != null
        ? System.getenv("KAFKA_BOOTSTRAP")
        : "kafka-broker:9092";
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<String, String>(props);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<String, String>(producerFactory());
  }

  // ----- Consumer -----
  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> props = new HashMap<String, Object>();
    String bootstrap = System.getenv("KAFKA_BOOTSTRAP") != null
        ? System.getenv("KAFKA_BOOTSTRAP")
        : "kafka-broker:9092";
    String groupId = System.getenv("REPLY_GROUP") != null
        ? System.getenv("REPLY_GROUP")
        : "webapp-reply";

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return new DefaultKafkaConsumerFactory<String, String>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> f =
        new ConcurrentKafkaListenerContainerFactory<String, String>();
    f.setConsumerFactory(consumerFactory());
    return f;
  }
}
