package com.github.ksewen.performance.test.gateway.util;

import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 30.07.2023 16:56
 */
public class UUIDUtils {

  /** Format von UUID mit „-“ */
  private static final String LONG_UUID =
      "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
  /** Format von UUID ohne „-“ */
  private static final String SHORT_UUID =
      "^[0-9a-f]{8}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{12}$";

  public static boolean isValidUUID(String uuid) {
    if (!StringUtils.hasLength(uuid)) {
      return false;
    }
    return uuid.matches(LONG_UUID) || uuid.matches(SHORT_UUID);
  }

  /**
   * UUID ohne „-“ generieren
   *
   * @return short uuid
   */
  public static String getShortUUID() {
    return getUUID().replace("-", "");
  }

  /**
   * UUID mit „-“ generieren
   *
   * @return uuid
   */
  public static String getUUID() {
    return UUID.randomUUID().toString();
  }
}
