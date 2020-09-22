package com.bet.coupon;

import com.bet.coupon.entity.Event;
import com.bet.coupon.enums.EventType;
import com.bet.coupon.repository.EventRepository;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.ApplicationScope;


@ApplicationScope
public class InitData {

  @Autowired
  private EventRepository eventRepository;

  @PostConstruct
  public void init() {

  }
}
