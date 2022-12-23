package com.gfc.bot01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class Bot01Application {
  
  public static void main(String[] args) {
    SpringApplication.run(Bot01Application.class, args);
  }
}
