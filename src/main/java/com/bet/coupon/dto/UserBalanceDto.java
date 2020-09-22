package com.bet.coupon.dto;

import java.math.BigDecimal;

public class UserBalanceDto {

  private BigDecimal amount;
  private TargetSource targetSource;
  private BalanceSource balanceSource;
  private String userId;

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public TargetSource getTargetSource() {
    return targetSource;
  }

  public void setTargetSource(TargetSource targetSource) {
    this.targetSource = targetSource;
  }

  public BalanceSource getBalanceSource() {
    return balanceSource;
  }

  public void setBalanceSource(BalanceSource  balanceSource) {
    this.balanceSource = balanceSource;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public enum BalanceSource {
    CARD, DEBIT, EFT, COUPON
  }


  public enum TargetSource {
    PreParedCoupon
  }

}
