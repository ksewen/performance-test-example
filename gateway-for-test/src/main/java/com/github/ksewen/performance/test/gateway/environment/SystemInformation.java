package com.github.ksewen.performance.test.gateway.environment;

/**
 * @author ksewen
 * @date 16.08.2023 12:31
 */
public interface SystemInformation {

  String getHostName();

  String getHostIp();

  String getApplicationName();

  String getAppId();
}
