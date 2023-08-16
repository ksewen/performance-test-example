package com.github.ksewen.performance.test.gateway.environment.impl;

import com.github.ksewen.performance.test.gateway.environment.SystemInformation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author ksewen
 * @date 16.08.2023 12:39
 */
@Slf4j
public class BasicSystemInformation implements SystemInformation {

  private String hostName;

  private String hostIp;

  @Value("${spring.application.name:UNSET}")
  private String applicationName;

  @Override
  public String getHostName() {
    return this.hostName;
  }

  @Override
  public String getHostIp() {
    return this.hostIp;
  }

  @Override
  public String getApplicationName() {
    return this.applicationName;
  }

  @Override
  public String getAppId() {
    return System.getProperty("JAVA_APP_ID", "UNSET");
  }

  private static BasicSystemInformation newInstance() {
    BasicSystemInformation basicSystemInformation = new BasicSystemInformation();
    basicSystemInformation.initHostInfo();
    return basicSystemInformation;
  }

  private void initHostInfo() {
    try {
      InetAddress address = InetAddress.getLocalHost();
      this.hostName = address.getHostName();
      this.hostIp = address.getHostAddress();
    } catch (UnknownHostException e) {
      log.error("Can not init hostName and hostIp", e);
    }
  }

  public static final class SingletonHolder {
    public static final BasicSystemInformation instance = BasicSystemInformation.newInstance();
  }

  public static BasicSystemInformation getInstance() {
    return SingletonHolder.instance;
  }
}
