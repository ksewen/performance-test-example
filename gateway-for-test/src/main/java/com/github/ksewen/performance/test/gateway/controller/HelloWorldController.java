package com.github.ksewen.performance.test.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 30.07.2023 15:09
 */
@RestController
public class HelloWorldController {

  @RequestMapping("/hello")
  public String hello() {
    return "Hello Gateway!";
  }
}
