package com.interplug.qcast.biz.login;

import com.interplug.qcast.biz.login.dto.LoginUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
interface LoginMapper {
  public boolean getLogin(LoginUser loginUser);
}
