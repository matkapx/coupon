package com.bet.coupon.repository;

import com.bet.coupon.entity.Coupon;
import com.bet.coupon.entity.CouponSelection;
import com.bet.coupon.entity.Event;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponSelectionRepository extends JpaRepository<CouponSelection, Long> {

  Optional<CouponSelection> findByEventAndCoupon(Event event, Coupon coupon);
}
