package com.bet.coupon.service;

import com.bet.coupon.dto.UserBalanceDto;
import com.bet.coupon.dto.UserBalanceDto.BalanceSource;
import com.bet.coupon.dto.UserBalanceDto.TargetSource;
import com.bet.coupon.entity.Coupon;
import com.bet.coupon.entity.CouponSelection;
import com.bet.coupon.entity.Event;
import com.bet.coupon.enums.CouponStatus;
import com.bet.coupon.enums.EventType;
import com.bet.coupon.repository.CouponRepository;
import com.bet.coupon.repository.CouponSelectionRepository;
import com.bet.coupon.repository.EventRepository;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(rollbackFor = Exception.class)
public class CouponService {

  private static final Long CUT_OF_BUFFER = 300L;
  private EventRepository eventRepository;
  private CouponRepository couponRepository;
  private CouponSelectionRepository couponSelectionRepository;
  private RestTemplate restTemplate;

  public CouponService(@Autowired EventRepository eventRepository, @Autowired CouponRepository couponRepository,
      @Autowired CouponSelectionRepository couponSelectionRepository, @Autowired RestTemplate restTemplate) {
    this.couponRepository = couponRepository;
    this.eventRepository = eventRepository;
    this.couponSelectionRepository = couponSelectionRepository;
    this.restTemplate = restTemplate;
  }

  public Long createNewCoupon() {
    Coupon coupon = new Coupon();
    coupon.setCouponStatus(CouponStatus.INITIAL);
    coupon.setCost(BigDecimal.valueOf(5d));
    Coupon save = couponRepository.save(coupon);
    return save.getId();
  }

  public Long saveCoupon(final Long couponId) {
    Coupon coupon = couponRepository.findById(couponId).map(x -> {
      if (validateCouponMbs(x)) {
        x.setCouponStatus(CouponStatus.ON_SALE);
      }
      return x;
    }).orElseThrow(() -> new RuntimeException("Coupon not found"));
    return coupon.getId();
  }

  public void addToCoupon(final Long eventId, final Long couponId) {
    Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);

    Coupon coupon = optionalCoupon.orElseThrow(() -> new RuntimeException("Coupon not found"));
    Optional<Event> eventOptional = eventRepository.findById(eventId);
    Event event = eventOptional.map(ev -> isPastEvent(ev))
        .map(ev-> isValidCoupon(coupon , ev))
        .map(ev -> isValidEventType(coupon, ev)).orElseThrow(() -> new RuntimeException("Event not found"));

    couponSelectionRepository.findByEventAndCoupon(event, coupon)
        .ifPresent(s -> {
          throw new RuntimeException("Event already in Coupon");
        });

    if (coupon.getMaximumMbs() < event.getMbs()) {
      coupon.setMaximumMbs(event.getMbs());
      couponRepository.save(coupon);
    }

    CouponSelection selection = new CouponSelection();
    selection.setCoupon(coupon);
    selection.setEvent(event);
    couponSelectionRepository.save(selection);

  }

  private Event isValidCoupon(Coupon coupon , Event event){
    if(coupon.getCouponStatus() != CouponStatus.INITIAL){
      throw new RuntimeException("Coupon is  " + coupon.getCouponStatus() + " can not be edited");
    }
    return event;

  }

  private Event isValidEventType(final Coupon coupon, final Event event) {
    if (coupon.getType() == null) {
      coupon.setType(event.getType());
      couponRepository.save(coupon);
    }
    if (coupon.getType() == EventType.FOOTBALL && event.getType() == EventType.TENNIS) {
      throw new RuntimeException("Can not add event type " + event.getType());
    } else if (coupon.getType() == EventType.TENNIS && event.getType() == EventType.FOOTBALL) {
      throw new RuntimeException("Can not add event type " + event.getType());
    }
    return event;
  }

  private Event isPastEvent(final Event event) {
    if (event.getEventDate().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Can not add past event " + event.getName());
    }
    return event;
  }

  private boolean validateCouponMbs(final Coupon coupon) {
    if (coupon.getCoupons().size() < coupon.getMaximumMbs()) {
      throw new RuntimeException(coupon.getMaximumMbs() - coupon.getCoupons().size() + " bet is required");
    }
    return true;
  }

  public Page<Coupon> findAllByCouponStatus(final CouponStatus status, final Pageable page) {
    return couponRepository.findAllByCouponStatus(status, page);
  }

  public Page<Coupon> findAllByUserId(final String userId, final Pageable pageable) {
    return couponRepository.findAllByUserId(userId, pageable);
  }

  public void buyCoupon(List<Long> couponId, final String userId) {

    BigDecimal totalAmount = BigDecimal.valueOf(couponId.size() * 5);
    ResponseEntity<BigDecimal> userBalance = restTemplate
        .postForEntity("http://localhost:8085/balance/user", userId, BigDecimal.class);

    if (userBalance.getStatusCode() == HttpStatus.OK) {
      if (userBalance.getBody().compareTo(totalAmount) >= 0) {

        UserBalanceDto dto = new UserBalanceDto();
        dto.setAmount(totalAmount.negate());
        dto.setUserId(userId);
        dto.setTargetSource(TargetSource.PreParedCoupon);
        ResponseEntity<Void> responseEntity = restTemplate
            .postForEntity("http://localhost:8085/balance/user/addBalance", dto, Void.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
          try {
            couponRepository.buyCoupon(couponId, userId, CouponStatus.SOLD_OUT);
          } catch (Exception e) {
            if (e instanceof PessimisticLockingFailureException) {
              dto = new UserBalanceDto();
              dto.setAmount(totalAmount);
              dto.setUserId(userId);
              dto.setBalanceSource(BalanceSource.COUPON);
              restTemplate
                  .postForEntity("http://localhost:8085/balance/user/addBalance", dto, Void.class);
            }
          }
        }

      } else {
        throw new RuntimeException("User Balance " + userBalance.getBody() + " is less than total amount " + totalAmount);
      }

    }
  }

  public void cancelCoupon(final Long couponId , final String userId) {
    couponRepository.findById(couponId).map(x -> {
      if (x.getPlayDate() != null && x.getCouponStatus() == CouponStatus.SOLD_OUT) {
        long seconds = Duration.between(x.getPlayDate(), LocalDateTime.now()).getSeconds();
        if (seconds > CUT_OF_BUFFER) {
          throw new RuntimeException("Coupon can not be canceled after " + TimeUnit.SECONDS.toMinutes(CUT_OF_BUFFER));
        }
        x.setPlayDate(null);
        x.setUserId(null);
        x.setCouponStatus(CouponStatus.ON_SALE);

        UserBalanceDto dto = new UserBalanceDto();
        dto.setAmount(x.getCost());
        dto.setUserId(userId);
        dto.setBalanceSource(BalanceSource.COUPON);
        ResponseEntity<Void> responseEntity = restTemplate
            .postForEntity("http://localhost:8085/balance/user/addBalance", dto, Void.class);

      }
      return x;
    });



  }
}
