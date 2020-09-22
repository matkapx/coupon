package com.bet.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CouponApplication {

  public static void main(String[] args) {
    SpringApplication.run(CouponApplication.class, args);
  }

  @Bean
  public InitData initData() {
    return new InitData();
  }

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }
}
