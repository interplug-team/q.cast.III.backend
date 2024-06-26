package com.interplug.qcast.biz.login;

import com.interplug.qcast.biz.login.dto.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {
  @Autowired LoginMapper loginMapper;

  public boolean getLogin(LoginUser loginUser) {
    return loginMapper.getLogin(loginUser);
  }
}
