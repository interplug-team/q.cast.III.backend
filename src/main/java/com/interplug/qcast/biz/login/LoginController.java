package com.interplug.qcast.biz.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

  @PostMapping("/v1.0/login")
  public void login(String username, String password) {}
}
