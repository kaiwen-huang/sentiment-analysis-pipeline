// SentimentController.java
package com.sa.web;

import org.springframework.web.bind.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*")
public class SentimentController {

  // Kafka producer
  private final KafkaTemplate<String, String> kafka;
  // In-memory waiter to correlate request-reply
  private final ReplyWaiter waiter;
  // JSON serializer
  private final ObjectMapper mapper = new ObjectMapper();
  // Topic name from env with default
  private final String REQ_TOPIC =
      System.getenv("REQ_TOPIC") != null ? System.getenv("REQ_TOPIC") : "sa.requests";

  public SentimentController(KafkaTemplate<String, String> kafka, ReplyWaiter waiter) {
    this.kafka = kafka;
    this.waiter = waiter;
  }

  // Send a request to Kafka and wait for the reply
  @PostMapping("/sentiment")
  public String sentiment(@RequestBody Map<String, String> body) throws Exception {
    // 1) read input
    String sentence = body != null && body.get("sentence") != null ? body.get("sentence") : "";
    String cid = UUID.randomUUID().toString();

    // 2) build payload (Java 8 style)
    Map<String, Object> payload = new HashMap<String, Object>();
    payload.put("sentence", sentence);
    payload.put("correlationId", cid);

    // 3) send to Kafka with key = correlationId
    String json = mapper.writeValueAsString(payload);
    kafka.send(REQ_TOPIC, cid, json);

    // 4) wait for reply (timeout 5s)
    String reply = waiter.await(cid, 5000L);

    // 5) if timeout, return a minimal JSON indicating timeout
    if (reply == null) {
      Map<String, Object> timeout = new HashMap<String, Object>();
      timeout.put("sentence", sentence);
      timeout.put("polarity", null);
      timeout.put("error", "timeout");
      timeout.put("correlationId", cid);
      return mapper.writeValueAsString(timeout);
    }

    // 6) reply is already a JSON string from consumer
    return reply;
  }
}
