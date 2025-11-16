package com.sa.web;

import java.util.concurrent.*;
import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class ReplyWaiter {
  private final ConcurrentHashMap<String, CompletableFuture<String>> pending = new ConcurrentHashMap<>();

  public String await(String correlationId, long timeoutMs) throws Exception {
    CompletableFuture<String> fut = new CompletableFuture<>();
    pending.put(correlationId, fut);
    try { return fut.get(timeoutMs, TimeUnit.MILLISECONDS); }
    finally { pending.remove(correlationId); }
  }

  public void fulfill(String correlationId, String payload) {
    CompletableFuture<String> fut = pending.get(correlationId);
    if (fut != null) fut.complete(payload);
  }
}
