package com.github.ksewen.performance.test.gateway.filter.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author ksewen
 * @date 30.07.2023 21:05
 */
@Component
@Slf4j
public class SimpleAuthGatewayFilterFactory
    extends AbstractGatewayFilterFactory<SimpleAuthGatewayFilterFactory.Config> {

  @Autowired private WebClient webClient;

  public SimpleAuthGatewayFilterFactory() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) ->
        this.webClient
            .get()
            .uri(
                UriComponentsBuilder.fromUriString(config.getUrl())
                    .queryParam("token", "111111")
                    .toUriString())
            .retrieve()
            .bodyToMono(Boolean.class)
            .flatMap(
                result -> {
                  if (!result) {
                    return Mono.error(new RuntimeException());
                  }
                  return chain.filter(exchange);
                });
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Config {
    private String url;
  }
}
