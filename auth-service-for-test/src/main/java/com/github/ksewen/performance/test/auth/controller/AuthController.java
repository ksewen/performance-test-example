package com.github.ksewen.performance.test.auth.controller;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 05.08.2023 23:16
 */
@RestController
@Slf4j
public class AuthController {

  @RequestMapping("/auth")
  public Boolean auth(@RequestParam String token) throws InterruptedException {
    double num = Math.random();
    int time = (int) (num * 100 + 1);
    log.debug("the auth process will cost {}ms", time);
    TimeUnit.MICROSECONDS.sleep(time);
    return true;
  }
}
