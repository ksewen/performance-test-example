package com.github.ksewen.performance.test.gateway.configuration;

import com.github.ksewen.performance.test.gateway.environment.SystemInformation;
import com.github.ksewen.performance.test.gateway.environment.impl.BasicSystemInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 16.08.2023 12:45
 */
@Configuration
public class SystemInformationAutoConfiguration {

  @Bean
  public SystemInformation systemInformation() {
    return BasicSystemInformation.getInstance();
  }
}
