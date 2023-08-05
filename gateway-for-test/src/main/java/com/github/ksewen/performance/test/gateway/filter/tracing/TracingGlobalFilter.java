package com.github.ksewen.performance.test.gateway.filter.tracing;

import com.github.ksewen.performance.test.gateway.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ksewen
 * @date 30.07.2023 16:47
 */
@Component
@Slf4j
public class TracingGlobalFilter implements GlobalFilter, Ordered {

  private final String REQUEST_ID = "requestId";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String requestId = UUIDUtils.getShortUUID();
    exchange.getAttributes().put(REQUEST_ID, requestId);
    log.info("request start...with id: {}", requestId);
    return chain
        .filter(exchange)
        .then(Mono.fromRunnable(() -> log.info("request end...with id: {}", requestId)));
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
