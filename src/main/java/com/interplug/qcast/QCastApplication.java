package com.interplug.qcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class QCastApplication {

  public static void main(String[] args) {
    SpringApplication.run(QCastApplication.class, args);
  }
}
