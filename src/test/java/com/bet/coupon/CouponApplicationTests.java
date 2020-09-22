package com.bet.coupon;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.bet.coupon.entity.Coupon;
import com.bet.coupon.entity.Event;
import com.bet.coupon.enums.CouponStatus;
import com.bet.coupon.enums.EventType;
import com.bet.coupon.repository.CouponRepository;
import com.bet.coupon.repository.EventRepository;
import com.bet.coupon.service.CouponService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CouponApplicationTests {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private CouponService couponService;

  @Autowired
  private RestTemplate restTemplate;

  private MockRestServiceServer mockServer;

  @BeforeEach
  void contextLoads() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
    Event ev = new Event();
    ev.setName("Football 1 ");
    ev.setType(EventType.FOOTBALL);
    ev.setMbs(1);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Football 2 ");
    ev.setType(EventType.FOOTBALL);
    ev.setMbs(1);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Football 3 ");
    ev.setType(EventType.FOOTBALL);
    ev.setMbs(1);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Football 4 ");
    ev.setType(EventType.FOOTBALL);
    ev.setMbs(2);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Football 5 ");
    ev.setType(EventType.FOOTBALL);
    ev.setMbs(2);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Football 5 ");
    ev.setType(EventType.FOOTBALL);
    ev.setMbs(2);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Football 6 ");
    ev.setType(EventType.FOOTBALL);
    ev.setMbs(2);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Football 7 ");
    ev.setType(EventType.FOOTBALL);
    ev.setMbs(3);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Basketball 1 ");
    ev.setType(EventType.BASKETBALL);
    ev.setMbs(1);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Basketball 2 ");
    ev.setType(EventType.BASKETBALL);
    ev.setMbs(2);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Basketball 3 ");
    ev.setType(EventType.BASKETBALL);
    ev.setMbs(2);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Basketball 4 ");
    ev.setType(EventType.BASKETBALL);
    ev.setMbs(2);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Basketball 5 ");
    ev.setType(EventType.BASKETBALL);
    ev.setMbs(3);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Tennis 1 ");
    ev.setType(EventType.TENNIS);
    ev.setMbs(1);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);

    ev = new Event();
    ev.setName("Tennis 2 ");
    ev.setType(EventType.TENNIS);
    ev.setMbs(1);
    ev.setEventDate(LocalDateTime.now().plusDays(10));
    eventRepository.save(ev);
  }

  @Test
  void testCreateCouponIniti() {
    Long newCoupon = couponService.createNewCoupon();
    Assertions.assertEquals(1L, newCoupon);
  }

  @Test
  void testInitialEventSize() {
    List<Event> events = eventRepository.findAll();
    Assertions.assertEquals(15, events.size());
  }

  @Test
  void saveCouponWithoutValidCoupon() {
    RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
      couponService.saveCoupon(10L);
    });
    Assertions.assertEquals("Coupon not found", ex.getMessage());
  }

  @Test
  void addToCouponTestToANewCoupon() {
    Long newCouponId = couponService.createNewCoupon();
    couponService.addToCoupon(1L, newCouponId);

    Coupon coupon = couponRepository.findById(newCouponId).get();
    Assertions.assertEquals(1, coupon.getMaximumMbs());

    couponService.addToCoupon(4L, newCouponId);
    coupon = couponRepository.findById(newCouponId).get();
    Assertions.assertEquals(2, coupon.getMaximumMbs());
  }

  @Test
  void validateSaveCouponWithMbsRule() {
    Long newCouponId = couponService.createNewCoupon();
    couponService.addToCoupon(4L, newCouponId);

    RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
      couponService.saveCoupon(newCouponId);
      ;
    });
    Assertions.assertEquals("1 bet is required", ex.getMessage());
  }

  @Test
  void validateSavedCouponStatus() {
    Long newCouponId = couponService.createNewCoupon();
    couponService.addToCoupon(1L, newCouponId);
    Long aLong = couponService.saveCoupon(newCouponId);
    couponRepository.findById(aLong)
        .ifPresent(z -> Assertions.assertEquals(CouponStatus.ON_SALE, z.getCouponStatus()));
  }

  @Test
  void validatePastEventCanNotBeAddedToCoupon() {
    Event ev = new Event();
    ev.setName("Past Event");
    ev.setType(EventType.BASKETBALL);
    ev.setMbs(2);
    ev.setEventDate(LocalDateTime.now().minusSeconds(2));
    Event save = eventRepository.save(ev);
    Long newCouponId = couponService.createNewCoupon();

    RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
      couponService.addToCoupon(save.getId(), newCouponId);
      ;
    });
    Assertions.assertEquals("Can not add past event Past Event", ex.getMessage());
  }

  @Test
  void whenUserBalanceGreaterThanCouponsAmountThenBuyCoupon() throws URISyntaxException {
    Long newCouponId = couponService.createNewCoupon();
    couponService.addToCoupon(1L, newCouponId);
    Long aLong = couponService.saveCoupon(newCouponId);

    newCouponId = couponService.createNewCoupon();
    couponService.addToCoupon(1L, newCouponId);
    couponService.saveCoupon(newCouponId);

    List<Long> coupons = new ArrayList<>();
    coupons.add(1L);
    coupons.add(2L);

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8085/balance/user")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("150")
        );

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8085/balance/user/addBalance")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
        );

    couponService.buyCoupon(coupons, "123");
    mockServer.verify();

    Assertions.assertEquals(CouponStatus.SOLD_OUT, couponRepository.findById(1l).get().getCouponStatus());
    Assertions.assertEquals(CouponStatus.SOLD_OUT, couponRepository.findById(2l).get().getCouponStatus());
  }

  @Test
  void whenUserLessGreaterThanCouponsAmountThenBuyCoupon() throws URISyntaxException {
    Long newCouponId = couponService.createNewCoupon();
    couponService.addToCoupon(1L, newCouponId);
    Long aLong = couponService.saveCoupon(newCouponId);

    List<Long> coupons = new ArrayList<>();
    coupons.add(1L);

    mockServer.expect(ExpectedCount.once(),
        requestTo(new URI("http://localhost:8085/balance/user")))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withStatus(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("2")
        );

    RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
      couponService.buyCoupon(coupons, "123");
    });

    Assertions.assertEquals("User Balance 2 is less than total amount 5", ex.getMessage());

    mockServer.verify();

  }

}
