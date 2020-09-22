package com.bet.coupon.entity;

import com.bet.coupon.enums.CouponStatus;
import com.bet.coupon.enums.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Coupon extends BaseEntity {


  @Id
  @SequenceGenerator(name = "GEN_COUPON", sequenceName = "SEQ_COUPON", allocationSize = 1)
  @GeneratedValue(generator = "GEN_COUPON", strategy = GenerationType.SEQUENCE)
  @Column(name = "ID", nullable = false)
  private Long id;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "STATUS")
  @Enumerated(EnumType.ORDINAL)
  private CouponStatus couponStatus = CouponStatus.INITIAL;
  @Column(name = "COST")
  private BigDecimal cost;
  @Column(name = "PLAY_DATE")
  private LocalDateTime playDate;
  @Column(name = "MAX_MBS")
  private int maximumMbs = 0;
  @Column(name = "EVENT_TYPE")
  @Enumerated(EnumType.ORDINAL)
  private EventType type;
  @JsonIgnore
  @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY)
  Set<CouponSelection> coupons;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public CouponStatus getCouponStatus() {
    return couponStatus;
  }

  public void setCouponStatus(CouponStatus couponStatus) {
    this.couponStatus = couponStatus;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public LocalDateTime getPlayDate() {
    return playDate;
  }

  public void setPlayDate(LocalDateTime playDate) {
    this.playDate = playDate;
  }

  public Set<CouponSelection> getCoupons() {
    return coupons;
  }

  public void setCoupons(Set<CouponSelection> coupons) {
    this.coupons = coupons;
  }

  public int getMaximumMbs() {
    return maximumMbs;
  }

  public void setMaximumMbs(int maximumMbs) {
    this.maximumMbs = maximumMbs;
  }

  public EventType getType() {
    return type;
  }

  public void setType(EventType type) {
    this.type = type;
  }
}
