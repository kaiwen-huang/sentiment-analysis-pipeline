// English comments only
package com.sa.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReplyListener {
  private final ReplyWaiter waiter;
  private final ObjectMapper mapper = new ObjectMapper();

  public ReplyListener(ReplyWaiter w) { this.waiter = w; }

  // group.id is set via ConsumerFactory in KafkaConfig
  @KafkaListener(topics = "${REPLY_TOPIC:sa.replies}")
  public void onMessage(String msg) throws Exception {
    JsonNode node = mapper.readTree(msg);
    String cid = node.get("correlationId").asText();
    waiter.fulfill(cid, msg);
  }
}
