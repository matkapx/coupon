package com.bet.coupon.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class CouponSelection extends BaseEntity {

  @Id
  @SequenceGenerator(name = "GEN_COUPON_SELECTION", sequenceName = "SEQ_COUPON_SELECTION", allocationSize = 1)
  @GeneratedValue(generator = "GEN_COUPON_SELECTION", strategy = GenerationType.SEQUENCE)
  @Column(name = "ID", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "event_id")
  private Event event;

  @ManyToOne
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public void setCoupon(Coupon coupon) {
    this.coupon = coupon;
  }
}
