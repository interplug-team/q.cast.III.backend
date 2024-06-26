package com.interplug.qcast.biz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/main")
public class MainController {

  @GetMapping("/v1.0")
  public String main() {
    if (log.isDebugEnabled()) {
      log.debug("MainController");
    }
    return "Main";
  }
}
