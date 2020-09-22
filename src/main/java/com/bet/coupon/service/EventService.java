package com.bet.coupon.service;

import com.bet.coupon.entity.Event;
import com.bet.coupon.repository.CouponRepository;
import com.bet.coupon.repository.CouponSelectionRepository;
import com.bet.coupon.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventService {

  private EventRepository eventRepository;

  public EventService(@Autowired EventRepository eventRepository, @Autowired CouponRepository couponRepository,
      @Autowired CouponSelectionRepository couponSelectionRepository) {
    this.eventRepository = eventRepository;
  }

  public Page<Event> findAll(Pageable pageable) {
    return eventRepository.findAll(pageable);
  }

}
