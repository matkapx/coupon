package com.bet.coupon.controller;

import com.bet.coupon.entity.Coupon;
import com.bet.coupon.enums.CouponStatus;
import com.bet.coupon.service.CouponService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/coupon")
public class CouponController {

  private CouponService couponService;


  public CouponController(@Autowired CouponService couponService) {
    this.couponService = couponService;
  }

  @GetMapping("/findAllByStatus")
  public Page<Coupon> findByStatus(final CouponStatus status, final Pageable pageable) {
    return couponService.findAllByCouponStatus(status, pageable);
  }

  @GetMapping("/findAllByUserId")
  public Page<Coupon> findAllByUserId(final String userId, final Pageable pageable) {
    return couponService.findAllByUserId(userId, pageable);
  }

  @PostMapping
  public Long createNewCoupon() {
    return couponService.createNewCoupon();
  }

  @PostMapping("/save")
  public Long saveCoupon(Long couponId) {
    return couponService.saveCoupon(couponId);
  }

  @PostMapping("/addToCoupon/{couponId}/{eventId}")
  public void addEventToCoupon(@PathVariable(name = "couponId") Long couponId,
      @PathVariable(name = "eventId") Long eventId) {
    couponService.addToCoupon(eventId, couponId);
  }

  @PostMapping("/buy")
  public void buyCoupoun(@RequestBody List<Long> couponId, @RequestParam String userId) {
    couponService.buyCoupon(couponId, userId);
  }

  @PostMapping("/cancel")
  public void buyCoupoun(@RequestParam Long couponId , @RequestParam String userId) {
    couponService.cancelCoupon(couponId, userId);
  }

}
