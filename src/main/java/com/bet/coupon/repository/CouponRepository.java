package com.bet.coupon.repository;

import com.bet.coupon.entity.Coupon;
import com.bet.coupon.enums.CouponStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  Page<Coupon> findAllByCouponStatus(CouponStatus status, Pageable pageable);

  Page<Coupon> findAllByUserId(String userId, Pageable pageable);

  @Modifying
  @Query(
      "UPDATE Coupon set userId= :userId, couponStatus=:copunStatus , playDate = SYSTIMESTAMP, updatedAt = SYSTIMESTAMP , version = version + 1 "
          + " WHERE ID IN (:couponId) and USER_ID is null and couponStatus=0")
  void buyCoupon(@Param("couponId") List<Long> couponId, @Param("userId") String userId,
      @Param("copunStatus") CouponStatus copunStatus);
}
