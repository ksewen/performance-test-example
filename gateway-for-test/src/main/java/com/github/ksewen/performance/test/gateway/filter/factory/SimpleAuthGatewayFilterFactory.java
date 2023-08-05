package com.github.ksewen.performance.test.gateway.filter.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author ksewen
 * @date 30.07.2023 21:05
 */
@Component
public class SimpleAuthGatewayFilterFactory
    extends AbstractGatewayFilterFactory<SimpleAuthGatewayFilterFactory.Config> {

  @Autowired private RestTemplate restTemplate;

  public SimpleAuthGatewayFilterFactory() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      String uri =
          UriComponentsBuilder.fromHttpUrl(config.getUrl())
              .queryParam("token", "111111")
              .toUriString();
      ResponseEntity<Boolean> result = restTemplate.postForEntity(uri, null, Boolean.class);
      return chain.filter(exchange);
    };
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Config {
    private String url;
  }
}
