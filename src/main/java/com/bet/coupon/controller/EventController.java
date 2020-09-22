package com.bet.coupon.controller;


import com.bet.coupon.entity.Event;
import com.bet.coupon.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

  private EventService eventService;

  public EventController(@Autowired EventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping("/all")
  public Page<Event> findAll(Pageable pageable) {
    return eventService.findAll(pageable);
  }


}
