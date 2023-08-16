package com.github.ksewen.performance.test.gateway.filter.tracing;

import com.github.ksewen.performance.test.gateway.environment.SystemInformation;
import com.github.ksewen.performance.test.gateway.util.UUIDUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ksewen
 * @date 30.07.2023 16:47
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TracingGlobalFilter implements GlobalFilter, Ordered {

  private final String REQUEST_ID = "requestId";

  private final String HOST_IP = "hostIp";

  private final String HOST_NAME = "hostName";

  private final String APPLICATION_NAME = "applicationName";

  private final String APP_ID = "appId";

  private final SystemInformation systemInformation;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String requestId = UUIDUtils.getShortUUID();
    exchange.getAttributes().put(REQUEST_ID, requestId);
    log.info("request start...with id: {}", requestId);

    ServerHttpRequest request =
        exchange
            .getRequest()
            .mutate()
            .header(this.REQUEST_ID, requestId)
            .header(this.HOST_IP, this.systemInformation.getHostIp())
            .header(this.HOST_NAME, this.systemInformation.getHostName())
            .header(this.APPLICATION_NAME, this.systemInformation.getApplicationName())
            .header(this.APP_ID, this.systemInformation.getAppId())
            .build();
    return chain
        .filter(exchange.mutate().request(request).build())
        .then(Mono.fromRunnable(() -> log.info("request end...with id: {}", requestId)));
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
