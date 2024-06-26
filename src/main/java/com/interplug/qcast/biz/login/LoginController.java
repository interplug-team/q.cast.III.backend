package com.interplug.qcast.biz.login;

import com.interplug.qcast.biz.login.dto.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {
  @Autowired private LoginService loginService;

  @PostMapping("/v1.0/login")
  public void login(@RequestBody LoginUser loginUser) {
    log.warn(String.valueOf(log.isDebugEnabled()));
    if (log.isDebugEnabled()) {
      log.debug("Login username: " + loginUser.getUsername());
      log.debug("Login password: " + loginUser.getPassword());
      log.debug("isLogin: " + loginService.getLogin(loginUser));
    }
  }
}
