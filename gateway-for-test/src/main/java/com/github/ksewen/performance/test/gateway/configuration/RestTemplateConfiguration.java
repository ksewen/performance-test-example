package com.github.ksewen.performance.test.gateway.configuration;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author ksewen
 * @date 01.08.2023 17:08
 */
@Configuration
public class RestTemplateConfiguration {

  @Bean
  RestTemplate restTemplate(@Autowired HttpClient httpClient) {
    return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
  }

  @Bean
  public CloseableHttpClient httpClient() {
    PoolingHttpClientConnectionManager connectionManager =
        PoolingHttpClientConnectionManagerBuilder.create()
            .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
            .setConnPoolPolicy(PoolReusePolicy.LIFO)
            .build();

    connectionManager.setDefaultSocketConfig(SocketConfig.custom().setTcpNoDelay(true).build());

    connectionManager.setDefaultConnectionConfig(
        ConnectionConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(1))
            .setSocketTimeout(Timeout.ofSeconds(5))
            .setValidateAfterInactivity(TimeValue.ofSeconds(10))
            .setTimeToLive(TimeValue.ofHours(1))
            .build());

    connectionManager.setMaxTotal(500);
    connectionManager.setDefaultMaxPerRoute(50);

    CloseableHttpClient httpClient =
        HttpClients.custom()
            .setConnectionManager(connectionManager)
            .evictIdleConnections(TimeValue.ofSeconds(60))
            .evictExpiredConnections()
            .build();

    return httpClient;
  }
}
